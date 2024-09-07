package com.unicam.Service.Contenuto;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.PuntoGeoRepository;
import com.unicam.Repository.IComuneRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.Service.ComuneService;
import com.unicam.dto.Risposte.PuntoGeoResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PuntoGeoService {

    private final IComuneRepository repoComune;
    private final PuntoGeoRepository repoPunto;
    private final UtenteRepository repoUtente;

    @Autowired
    public PuntoGeoService(PuntoGeoRepository repoPunto,
                           UtenteRepository repoUtente,
                           IComuneRepository repoComune){
        this.repoPunto = repoPunto;
        this.repoUtente = repoUtente;
        this.repoComune = repoComune;
    }

    /**
     * Insersci un nuovo PuntoGeolocalizzato. Il nuovo punto viene salvato nel database solo se non esistono
     * punti approvati con le stesse coordinate o stesso nome. Nel caso sia già presente un punto nel database
     * con stesso nome e/o stesse coordinate, il contenuto viene salvato solo se entrambi sono in stato di attesa.
     * La presenza di contenuti ripetuti in attesa verrà gestita al momento dell'approvazione di uno
     * degli oggetti presenti più volte.
     *
     * @param contenuto         punto da inserire nel database
     * @exception IllegalArgumentException se è gia stato approvato un punto con le stesse coordinate
     *                                      del punto che si sta provando ad aggiungere oppure se è già
     *                                      stato approvato un punto con lo stesso nome del punto passato
     *                                      ma con differenti coordinate
     */
    public void AggiungiContenuto(PuntoGeolocalizzato contenuto) {
        List<PuntoGeolocalizzato> puntiPresenti = this.repoPunto.findByComune(contenuto.getComune());
        for (PuntoGeolocalizzato puntoTrovato: puntiPresenti) {
            if(puntoTrovato.getStato() != StatoContenuto.ATTESA) {
                if ((puntoTrovato.getLatitudine().equals(contenuto.getLatitudine()))
                        && (puntoTrovato.getLongitudine().equals(contenuto.getLongitudine())))
                    throw new IllegalArgumentException("Esiste già un punto con quelle coordinate");
                if((puntoTrovato.getTitolo().equals(contenuto.getTitolo()))
                        && (puntoTrovato.getComune().equals(contenuto.getComune())))
                    throw new IllegalArgumentException("Esiste già un punto geolocalizzato con quel nome. " +
                            "Prova ad essere più specifico/a");
            }
        }
        this.repoPunto.save(contenuto);
    }

    public void ApprovaContenuto(long id, PuntoGeolocalizzato contenuto, StatoContenuto nuovoStato) {
        User user = repoUtente.getById(id);
        if (nuovoStato == StatoContenuto.APPROVATO) {
            contenuto.setStato(nuovoStato);
            repoPunto.save(contenuto);
        } else {
            repoPunto.delete(contenuto);
        }
    }

    public PuntoGeolocalizzato GetPuntoGeoByNome(String nome) {
        return this.repoPunto.findGeoByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public PuntoGeolocalizzato GetPuntoGeoByNomeAndComune(String nome, String comune) {
        return this.repoPunto.findGeoByTitoloAndComune(nome, comune);
    }

    public List<PuntoGeolocalizzato> GetPuntiByListaNomi(List<String> nomiPunti) {
        List<PuntoGeolocalizzato> punti = new ArrayList<>();
        for (String nome : nomiPunti) {
            punti.add(GetPuntoGeoByNome(nome.toUpperCase(Locale.ROOT)));
        }
        return punti;
    }

    public List<PuntoGeoResponseDTO> GetPuntiGeoByComune(String comune) {
        List<PuntoGeolocalizzato> puntiPresenti = this.repoPunto.findPuntoGeoByComune(comune);
        List<PuntoGeoResponseDTO> punti = new ArrayList<>();
        for (PuntoGeolocalizzato punto : puntiPresenti) {
            if (!(punto.getStato() == StatoContenuto.ATTESA))
                punti.add(new PuntoGeoResponseDTO(punto.getTitolo(), punto.getDescrizione(),
                        punto.getLatitudine(), punto.getLongitudine(), punto.getAutore().getUsername()));
        }
        return punti;
    }

    public List<PuntoGeoResponseDTO> GetPuntiGeoStatoByComune(String comune, StatoContenuto stato) {
        List<PuntoGeolocalizzato> puntiPresenti = this.repoPunto.findPuntoGeoByComune(comune);
        List<PuntoGeoResponseDTO> punti = new ArrayList<>();
        for (PuntoGeolocalizzato punto : puntiPresenti) {
            if (punto.getStato() == stato)
                punti.add(new PuntoGeoResponseDTO(punto.getTitolo(), punto.getDescrizione(),
                        punto.getLatitudine(), punto.getLongitudine(), punto.getAutore().getUsername()));
        }
        return punti;
    }

    public void AggiuntiPreferito(String nomeContenuto, Long idUtente) {
        PuntoGeolocalizzato punto = this.repoPunto.findGeoByTitolo(nomeContenuto);
        punto.getIdUtenteContenutoPreferito().add(idUtente);
        this.repoPunto.save(punto);
    }

    public void SegnalaContenuto(String nomeContenuto, long idCreatore) {
        PuntoGeolocalizzato punto = this.repoPunto.findGeoByTitolo(nomeContenuto);
        punto.setStato(StatoContenuto.SEGNALATO);
        this.repoPunto.save(punto);
    }

    public void AccettaORifiuta(String nomeContenuto, String comune, StatoContenuto stato) {
        if(!this.repoPunto.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.ATTESA))
            throw new IllegalArgumentException("Il punto non è presente tra le richieste. " +
                    "Si prega di controllare di aver scritto bene il nome e riprovare");
        List<PuntoGeolocalizzato> punti = this.repoPunto.findByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.ATTESA);
        PuntoGeolocalizzato punto = punti.get(0);
        //punti = this.repoPunto.findByComuneAndStato(comune, StatoContenuto.ATTESA);
        punti.clear();
        punti.addAll(this.repoPunto.findByStato(StatoContenuto.ATTESA));
        punti.remove(punto);
        if(stato == StatoContenuto.RIFIUTATO)
            this.repoPunto.delete(punto);
        else{
            punto.setStato(stato);
            this.repoPunto.save(punto);
            if(!punti.isEmpty())
                EliminaDoppioni(punti, punto);
        }
    }

    private void EliminaDoppioni(List<PuntoGeolocalizzato> punti, PuntoGeolocalizzato punto) {
        for(PuntoGeolocalizzato puntoTrovato: punti){

            if(puntoTrovato.getLatitudine().equals(punto.getLatitudine()) &&
                    puntoTrovato.getLongitudine().equals(punto.getLongitudine())){
                if(puntoTrovato.getTitolo().equals("COMUNE")){
                    //Comune comune = this.repoComune.findByNome(puntoTrovato.getComune());
                    this.repoComune.delete(this.repoComune.findByNome(puntoTrovato.getComune()));
                }
                this.repoPunto.delete(puntoTrovato);
            }

            else if(puntoTrovato.getTitolo().equals(punto.getTitolo()))
                this.repoPunto.delete(puntoTrovato);
        }
    }

    public void EliminaContenutiAttesaDoppioni(PuntoGeolocalizzato punto) {
        List<PuntoGeolocalizzato> puntiTrovati = new ArrayList<>();
        puntiTrovati.addAll(this.repoPunto.findByLatitudineAndLongitudineAndStato(punto.getLatitudine(), punto.getLongitudine(),StatoContenuto.ATTESA));
        this.repoPunto.deleteAll(puntiTrovati);
    }

    public PuntoGeolocalizzato GetPuntoGeoByNomeAndComuneAndStato(String nomePuntoGeo, String comune) {
        if (this.repoPunto.existsByTitoloAndComune(nomePuntoGeo, comune)) {
            List<PuntoGeolocalizzato> punti = this.repoPunto.findByTitoloAndComune(nomePuntoGeo, comune);
            for(PuntoGeolocalizzato punto : punti){
                if(punto.getStato() != StatoContenuto.ATTESA)
                    return punto;
            }
        }
        throw new IllegalArgumentException("Il punto non è ancora stato approvato");
    }

    public List<PuntoGeolocalizzato> GetPuntiByListaNomiAndComuneAndStato(List<String> nomiPunti, String comune) {
        List<PuntoGeolocalizzato> punti = new ArrayList<>();
        for(String nome: nomiPunti){
            punti.add(this.GetPuntoGeoByNomeAndComuneAndStato(nome.toUpperCase(Locale.ROOT), comune));
        }
        return punti;
    }

    public void ContienePunto(PuntoGeolocalizzato punto) {
        List<PuntoGeolocalizzato> punti = new ArrayList<>();
        punti.addAll(this.repoPunto.findAll());
        for(PuntoGeolocalizzato puntoTrovato : punti){
            if(puntoTrovato.getStato() != StatoContenuto.ATTESA)
                if(puntoTrovato.getLatitudine().equals(punto.getLatitudine()) &&
                        puntoTrovato.getLongitudine().equals(punto.getLongitudine()))
                    throw new IllegalArgumentException("Coordinate riferite ad un altro punto geolocalizzato già approvato");
        }
    }

    /**
     * Inserimento del punto inerente alla richiesta del comune, dopo aver controllato
     * che le coordinate non siano già associate ad un altro punto approvato
     * @param punto     punto da inserire
     */
    public void AggiungiPunto(PuntoGeolocalizzato punto) {
        this.repoPunto.save(punto);
    }
}
