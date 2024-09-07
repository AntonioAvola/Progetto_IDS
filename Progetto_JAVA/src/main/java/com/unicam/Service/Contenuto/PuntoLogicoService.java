package com.unicam.Service.Contenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.PuntoLogico;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import com.unicam.Repository.Contenuto.PuntoGeoRepository;
import com.unicam.Repository.Contenuto.PuntoLogicoRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.dto.Risposte.LuogoDTO;
import com.unicam.dto.Risposte.PuntoLogicoResponseDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class PuntoLogicoService {

    private final PuntoLogicoRepository repoPunto;
    private final PuntoGeoRepository repoGeo;
    private final UtenteRepository repoUtente;

    public PuntoLogicoService(PuntoLogicoRepository repoPunto,
                              PuntoGeoRepository repoGeo,
                              UtenteRepository repoUtente){
        this.repoPunto = repoPunto;
        this.repoGeo = repoGeo;
        this.repoUtente = repoUtente;
    }

    /**
     * Insersci un nuovo PuntoLogico. Il nuovo punto viene salvato nel database solo se non esistono
     * punti approvati con lo stesso riferimento. Nel caso sia già presente un punto nel database
     * con lo stesso riferimento, il contenuto viene salvato solo se entrambi sono in stato di attesa.
     * Più punti logici possono avere uno stesso riferimento, Ma devono avere un titolo differente.
     * La presenza di contenuti ripetuti in attesa verrà gestita al momento dell'approvazione di uno
     * degli oggetti presenti più volte.
     *
     * @param contenuto         punto da inserire nel database
     * @exception IllegalArgumentException se è gia stato approvato un punto con lo stesso riferimento
     *                                       del punto che si sta provando ad aggiungere
     */
    public void AggiungiContenuto(PuntoLogico contenuto) {
        if(this.repoPunto.existsByTitoloAndComune(contenuto.getTitolo(), contenuto.getComune())){
            List<PuntoLogico> puntiApprovati = this.repoPunto.findByTitoloAndComune(contenuto.getTitolo(), contenuto.getComune());
            for (PuntoLogico puntoTrovato: puntiApprovati) {
                if(puntoTrovato.getRiferimento() == contenuto.getRiferimento()  /*&& puntoTrovato.getRiferimento() == contenuto.getRiferimento()*/){
                    if(puntoTrovato.getStato() != StatoContenuto.ATTESA)
                        throw new IllegalArgumentException("Nel sistema è già presente questo avviso per questo specifico punto");
                }
            }
        }
        this.repoPunto.save(contenuto);
    }

    public void ApprovaContenuto(long id, PuntoLogico contenuto, StatoContenuto nuovoStato) {
        User user = repoUtente.getById(id);
        if (nuovoStato == StatoContenuto.APPROVATO) {
            contenuto.setStato(nuovoStato);
            repoPunto.save(contenuto);
        } else {
            repoPunto.delete(contenuto);
        }
    }

    public PuntoLogico GetPuntoLogicoByNome(String nome){
        return this.repoPunto.findLogicoByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public List<PuntoLogicoResponseDTO> GetPuntiLogiciByComune(String comune) {
        List<PuntoLogico> puntiPresenti = this.repoPunto.findPuntoLogicoByComune(comune);
        List<PuntoLogicoResponseDTO> punti = new ArrayList<>();
        for (PuntoLogico punto : puntiPresenti) {
            if (!(punto.getStato() == StatoContenuto.ATTESA)) {
                PuntoLogicoResponseDTO nuovo =
                        new PuntoLogicoResponseDTO(punto.getTitolo(), punto.getDescrizione(), punto.getAutore().getUsername());
                nuovo.setLuogo(ConvertiInLuogoDTO(punto.getRiferimento()));
                punti.add(nuovo);
            }
        }
        return punti;
    }

    private LuogoDTO ConvertiInLuogoDTO(PuntoGeolocalizzato riferimento) {
        return new LuogoDTO(riferimento.getTitolo(), riferimento.getLatitudine(), riferimento.getLongitudine());
    }

    public List<PuntoLogicoResponseDTO> GetPuntiLogiciStatoByComune(String comune, StatoContenuto stato) {
        List<PuntoLogico> puntiPresenti = this.repoPunto.findPuntoLogicoByComune(comune);
        List<PuntoLogicoResponseDTO> punti = new ArrayList<>();
        for (PuntoLogico punto : puntiPresenti) {
            if (punto.getStato() == stato) {
                PuntoLogicoResponseDTO nuovo =
                        new PuntoLogicoResponseDTO(punto.getTitolo(), punto.getDescrizione(), punto.getAutore().getUsername());
                nuovo.setLuogo(ConvertiInLuogoDTO(punto.getRiferimento()));
                punti.add(nuovo);
            }
        }
        return punti;
    }

    public void AggiungiPreferito(String nomeContenuto, Long idUtente) {
        PuntoLogico punto = this.repoPunto.findLogicoByTitolo(nomeContenuto);
        punto.getIdUtenteContenutoPreferito().add(idUtente);
        this.repoPunto.save(punto);
    }

    public void SegnalaContenuto(String nomeContenuto, long idCreatore) {
        PuntoLogico punto = this.repoPunto.findLogicoByTitolo(nomeContenuto);
        punto.setStato(StatoContenuto.SEGNALATO);
        this.repoPunto.save(punto);
    }

    public void AccettaORifiuta(String nomeContenuto, String comune, StatoContenuto stato) {
        if(!this.repoPunto.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.ATTESA))
            throw new IllegalArgumentException("Il punto non è presente tra le richieste. " +
                    "Si prega di controllare di aver scritto bene il nome e riprovare");
        List<PuntoLogico> punti = this.repoPunto.findByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.ATTESA);
        PuntoLogico punto = punti.get(0);
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

    private void EliminaDoppioni(List<PuntoLogico> punti, PuntoLogico punto) {
        for(PuntoLogico puntoTrovato: punti){
            if(puntoTrovato.getRiferimento().equals(punto.getRiferimento()))
                this.repoPunto.delete(puntoTrovato);
        }
    }

    public void EliminaContenutiAttesaDoppioni(PuntoLogico punto) {
        List<PuntoLogico> puntiTrovati = new ArrayList<>();
        puntiTrovati.addAll(this.repoPunto.findByTitoloAndComuneAndRiferimentoAndStato(punto.getTitolo(),
                punto.getComune(), punto.getRiferimento(), StatoContenuto.ATTESA));
        this.repoPunto.deleteAll(puntiTrovati);
    }
}
