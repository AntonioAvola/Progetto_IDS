package com.unicam.Service.Contenuto;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.EventoRepository;
import com.unicam.Repository.Contenuto.ItinerarioRepository;
import com.unicam.Repository.Contenuto.PuntoGeoRepository;
import com.unicam.Repository.Contenuto.PuntoLogicoRepository;
import com.unicam.Repository.IComuneRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.Service.ComuneService;
import com.unicam.dto.Risposte.ItinerarioResponseDTO;
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
    private final PuntoLogicoRepository repoLogico;
    private final EventoRepository repoEv;
    private final ItinerarioRepository repoIti;

    @Autowired
    public PuntoGeoService(PuntoGeoRepository repoPunto,
                           UtenteRepository repoUtente,
                           IComuneRepository repoComune,
                           PuntoLogicoRepository repoLogico,
                           EventoRepository repoEv,
                           ItinerarioRepository repoIti){
        this.repoPunto = repoPunto;
        this.repoUtente = repoUtente;
        this.repoComune = repoComune;
        this.repoLogico = repoLogico;
        this.repoEv = repoEv;
        this.repoIti = repoIti;
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
        List<PuntoGeolocalizzato> puntiPresenti = this.repoPunto.findAll();
        for (PuntoGeolocalizzato puntoTrovato: puntiPresenti) {
            if(puntoTrovato.getStato() == StatoContenuto.APPROVATO) {
                if ((puntoTrovato.getLatitudine().equals(contenuto.getLatitudine()))
                        && (puntoTrovato.getLongitudine().equals(contenuto.getLongitudine())))
                    throw new IllegalArgumentException("Esiste già un punto con quelle coordinate");
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

    public List<PuntoGeoResponseDTO> GetPuntiGeoByComune(String comune) {
        List<PuntoGeolocalizzato> puntiPresenti = this.repoPunto.findPuntoGeoByComune(comune);
        List<PuntoGeoResponseDTO> punti = new ArrayList<>();
        for (PuntoGeolocalizzato punto : puntiPresenti) {
            if (punto.getStato() == StatoContenuto.APPROVATO)
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

    public void AggiuntiPreferito(String nomeContenuto, String comune, Long idUtente) {
        if(!this.repoPunto.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.APPROVATO))
            throw new IllegalArgumentException("Il punto non esiste. Controlla di aver scritto bene le caratteristiche");
        PuntoGeolocalizzato punto = this.repoPunto.findGeoByTitoloAndComune(nomeContenuto, comune);
        List<Long> utentePreferito = punto.getIdUtenteContenutoPreferito();
        if(utentePreferito.contains(idUtente))
            throw new IllegalArgumentException("Il punto è già tra i preferiti");
        punto.getIdUtenteContenutoPreferito().add(idUtente);
        this.repoPunto.save(punto);
    }

    public void SegnalaContenuto(String nomeContenuto, String comune) {
        if(!this.repoPunto.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.APPROVATO)) {
            if(this.repoPunto.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.SEGNALATO))
                throw new IllegalArgumentException("Il punto è già stato segnalato");
            throw new IllegalArgumentException("Il punto non esiste. Controlla di aver scritto bene le caratteristiche");
        }
        PuntoGeolocalizzato punto = this.repoPunto.findGeoByTitoloAndComune(nomeContenuto, comune);
        punto.setStato(StatoContenuto.SEGNALATO);
        this.repoPunto.save(punto);
    }

    public void AccettaORifiuta(String nomeContenuto, String comune, StatoContenuto stato) {
        if(!this.repoPunto.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.ATTESA))
            if(!this.repoPunto.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.SEGNALATO))
                throw new IllegalArgumentException("Il punto non è presente tra le richieste o segnalazioni. " +
                        "Si prega di controllare di aver scritto bene il nome e riprovare");
        PuntoGeolocalizzato punto = this.repoPunto.findGeoByTitoloAndComune(nomeContenuto, comune);
        if(stato == StatoContenuto.RIFIUTATO) {
            EliminaDaItinerariLogiciEventi(punto);
            this.repoPunto.delete(punto);
        }
        else{
            punto.setStato(stato);
            this.repoPunto.save(punto);
            /**
             * Se il punto non viene rifiutato, mi salvo tutti i punti geolocalizzati che sono in stato di attesa,
             * levando il punto sottoposto ad accettazione, e se la lista non è vuota (quindi ci sono altri punti
             * in stato di attesa, anche di altri comuni) eseguo i controlli per verificare se tali putni in attesa
             * hanno lo stesso nome e/o coordinate del punto che viene accettato e nel caso coincidono, levo il
             * punto in attesa (non quello accettato) dal database.
             * Anche nel caso che sia stata fatta richiesta di aggiunta di un comune, se il comune ha le stesse
             * coordinate del punto accettato sotto altro comune, la richiesta di aggiunta dell'altro comune viene
             * eliminata dai comuni in attesa
             */
            List<PuntoGeolocalizzato> punti = this.repoPunto.findByComuneAndStato(comune, StatoContenuto.ATTESA);
            if(!punti.isEmpty())
                EliminaDoppioni(punti, punto);
        }
    }

    private void EliminaDaItinerariLogiciEventi(PuntoGeolocalizzato punto) {
        EliminaDaItinerari(punto);
        EliminaEventi(punto);
        EliminaLogici(punto);
    }

    private void EliminaLogici(PuntoGeolocalizzato punto) {
        List<PuntoLogico> punti = this.repoLogico.findByRiferimento(punto);
        this.repoLogico.deleteAll(punti);
    }

    private void EliminaEventi(PuntoGeolocalizzato punto) {
        List<Evento> eventi = this.repoEv.findEventoByComune(punto.getComune());
        for(Evento evento : eventi){
            if(evento.getLuogo().equals(punto)){
                this.repoEv.delete(evento);
            }
        }
    }

    private void EliminaDaItinerari(PuntoGeolocalizzato punto) {
        List<Itinerario> itinerari = this.repoIti.findItinerarioByComune(punto.getComune());
        for(Itinerario itinerario : itinerari){
            List<PuntoGeolocalizzato> punti = itinerario.getPuntiDiInteresse();
            if(punti.contains(punto)) {
                if(punti.size()==1)
                    this.repoIti.delete(itinerario);
                else{
                    punti.remove(punto);
                    itinerario.setPuntiDiInteresse(punti);
                    this.repoIti.save(itinerario);
                }
            }
        }
    }

    private void EliminaDoppioni(List<PuntoGeolocalizzato> punti, PuntoGeolocalizzato punto) {
        for(PuntoGeolocalizzato puntoTrovato: punti){
            if(puntoTrovato.getLatitudine().equals(punto.getLatitudine()) &&
                    puntoTrovato.getLongitudine().equals(punto.getLongitudine())){
                /**
                 * Elimino la richiesta di aggiunta di un comune (in attesa) qualora abbia le stesse coorinate
                 * del punto che viene approvato sotto comune differente dalla richiesta di aggiunta al sistema.
                 * Eliminando prima il comune e poi il suo riferimento geolocalizzato
                 */
                if(puntoTrovato.getTitolo().equals("COMUNE")){
                    this.repoComune.delete(this.repoComune.findByNome(puntoTrovato.getComune()));
                }
                this.repoPunto.delete(puntoTrovato);
            }
        }
    }

    public void EliminaContenutiAttesaDoppioni(PuntoGeolocalizzato punto) {
        List<PuntoGeolocalizzato> puntiTrovati = new ArrayList<>();
        puntiTrovati.addAll(this.repoPunto.findByLatitudineAndLongitudineAndStato(punto.getLatitudine(), punto.getLongitudine(),StatoContenuto.ATTESA));
        //puntiTrovati.addAll(this.repoPunto.findPuntiByTitoloAndComuneAndStato(punto.getTitolo(), punto.getComune(), StatoContenuto.ATTESA));
        for(PuntoGeolocalizzato puntoTrovato : puntiTrovati){
            if(punto.getTitolo().equals("COMUNE"))
                this.repoComune.delete(this.repoComune.findByNome(puntoTrovato.getComune()));
            this.repoPunto.delete(puntoTrovato);
        }
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

    public void ControllaPresenzaNome(String titolo, String comune) {
        if(this.repoPunto.findGeoByTitoloAndComune(titolo, comune) != null)
            throw new IllegalArgumentException("Esiste già un punto geolocalizzato (accettato, in attesa, o segnalato) con quel nome. " +
                    "Prova ad essere più specifico/a");
    }

    public List<PuntoGeoResponseDTO> GetPuntiPreferiti(Long idUtente, String nomeComune) {
        List<PuntoGeolocalizzato> punti = this.repoPunto.findByComuneAndStato(nomeComune, StatoContenuto.APPROVATO);
        List<PuntoGeoResponseDTO> puntiPreferiti = new ArrayList<>();
        for(PuntoGeolocalizzato punto: punti){
            if(punto.getIdUtenteContenutoPreferito().contains(idUtente))
                puntiPreferiti.add(new PuntoGeoResponseDTO(punto.getTitolo(), punto.getDescrizione(),
                        punto.getLatitudine(), punto.getLongitudine(), punto.getAutore().getUsername()));
        }
        return puntiPreferiti;
    }

    public List<PuntoGeoResponseDTO> GetPuntiGeoByAutore(User autore) {
        List<PuntoGeolocalizzato> punti = this.repoPunto.findByAutore(autore);
        List<PuntoGeoResponseDTO> puntiPropri = new ArrayList<>();
        if(punti != null){
            for(PuntoGeolocalizzato punto: punti){
                puntiPropri.add(new PuntoGeoResponseDTO(punto.getTitolo(), punto.getDescrizione(),
                        punto.getLatitudine(), punto.getLongitudine(), punto.getAutore().getUsername()));
            }
        }
        return puntiPropri;
    }

    public void EliminaPuntoGeo(String nomeContenuto, String comune) {
        if(!this.repoPunto.existsByTitoloAndComune(nomeContenuto, comune))
            throw new IllegalArgumentException("Il punto specificato non esiste. Controllare di aver inserito correttamente il nome");
        PuntoGeolocalizzato punto = this.repoPunto.findGeoByTitoloAndComune(nomeContenuto, comune);
        EliminaDaItinerariLogiciEventi(punto);
        this.repoPunto.delete(punto);
    }

    /*public PuntoGeolocalizzato GetPuntoGeoByNome(String nome) {
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
    }*/
}
