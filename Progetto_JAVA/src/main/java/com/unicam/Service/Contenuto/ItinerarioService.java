package com.unicam.Service.Contenuto;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.ItinerarioRepository;
import com.unicam.Repository.Contenuto.PuntoGeoRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.dto.Risposte.ItinerarioResponseDTO;
import com.unicam.dto.Risposte.LuogoDTO;
import com.unicam.dto.Risposte.PuntoGeoResponseDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ItinerarioService {

    private final ItinerarioRepository repoItinerario;
    private final PuntoGeoRepository repoPunto;
    private final UtenteRepository repoUtente;

    public ItinerarioService(ItinerarioRepository repoItinerario,
                             PuntoGeoRepository repoPunto,
                             UtenteRepository repoUtente){
        this.repoItinerario = repoItinerario;
        this.repoPunto = repoPunto;
        this.repoUtente = repoUtente;
    }

    /**
     * Insersci un nuovo Itinerario. Il nuovo itinerario viene salvato nel database solo se non esistono
     * itinerari approvati con la stessa identica lista di punti di interesse. Nel caso sia già presente
     * un itinerario nel database con la stessa lista di punti, l'itinerario viene salvato solo se entrambi
     * sono in stato di attesa.
     * La presenza di contenuti ripetuti in attesa verrà gestita al momento dell'approvazione di uno
     * degli oggetti presenti più volte.
     *
     * @param contenuto         itinerario da inserire nel database
     * @exception IllegalArgumentException se è gia stato approvato un itinerario con gli stessi punti di interesse
     *                                      dell'itinerario che si sta provando ad aggiungere
     */
    public void AggiungiContenuto(Itinerario contenuto) {
        List<Itinerario> itinerari = this.repoItinerario.findItinerarioByComune(contenuto.getComune());
        for (Itinerario itinerario : itinerari) {
            if(itinerario.getStato() == StatoContenuto.APPROVATO){
                if(itinerario.getPuntiDiInteresse().containsAll(contenuto.getPuntiDiInteresse())
                        && itinerario.getPuntiDiInteresse().size() == contenuto.getPuntiDiInteresse().size())
                    throw new IllegalArgumentException("Esiste già un itinerario con questi punti di interesse");
            }
        }
        this.repoItinerario.save(contenuto);
    }

    public List<ItinerarioResponseDTO> GetItinerariByComune(String comune) {
        List<Itinerario> itinerariPresenti = this.repoItinerario.findItinerarioByComune(comune);
        List<ItinerarioResponseDTO> itinerari = new ArrayList<>();
        for (Itinerario itinerario : itinerariPresenti) {
            if (itinerario.getStato() == StatoContenuto.APPROVATO) {
                ItinerarioResponseDTO nuovo = new ItinerarioResponseDTO(itinerario.getTitolo(),
                        itinerario.getDescrizione(), itinerario.getAutore().getUsername());
                nuovo.setLuoghi(ConvertiInListaDiLuoghiDTO(itinerario.getPuntiDiInteresse()));
                itinerari.add(nuovo);
            }
        }
        return itinerari;
    }

    private List<LuogoDTO> ConvertiInListaDiLuoghiDTO(List<PuntoGeolocalizzato> puntiDiInteresse) {
        List<LuogoDTO> luoghi = new ArrayList<>();
        for (PuntoGeolocalizzato punto : puntiDiInteresse) {
            luoghi.add(ConvertiInLuogoDTO(punto));
        }
        return luoghi;
    }

    private LuogoDTO ConvertiInLuogoDTO(PuntoGeolocalizzato riferimento) {
        return new LuogoDTO(riferimento.getTitolo(), riferimento.getLatitudine(), riferimento.getLongitudine());
    }

    public List<ItinerarioResponseDTO> GetItinerariStatoByComune(String comune, StatoContenuto stato) {
        List<Itinerario> itinerariPresenti = this.repoItinerario.findItinerarioByComune(comune);
        List<ItinerarioResponseDTO> itinerari = new ArrayList<>();
        for (Itinerario itinerario : itinerariPresenti) {
            if (itinerario.getStato() == stato) {
                ItinerarioResponseDTO nuovo = new ItinerarioResponseDTO(itinerario.getTitolo(),
                        itinerario.getDescrizione(), itinerario.getAutore().getUsername());
                nuovo.setLuoghi(ConvertiInListaDiLuoghiDTO(itinerario.getPuntiDiInteresse()));
                itinerari.add(nuovo);
            }
        }
        return itinerari;
    }

    public void AggiungiPreferito(String nomeContenuto, String comune, Long idUtente) {
        if(!this.repoItinerario.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.APPROVATO))
            throw new IllegalArgumentException("Il punto non esiste. Controlla di aver scritto bene le caratteristiche");
        Itinerario itinerario = this.repoItinerario.findItinerarioByTitoloAndComune(nomeContenuto, comune);
        List<Long> utentePreferito = itinerario.getIdUtenteContenutoPreferito();
        if(utentePreferito.contains(idUtente))
            throw new IllegalArgumentException("L'itinerario è già tra i preferiti");
        itinerario.getIdUtenteContenutoPreferito().add(idUtente);
        this.repoItinerario.save(itinerario);
    }

    public void SegnalaContenuto(String nomeContenuto, String comune) {
        if(!this.repoItinerario.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.APPROVATO)) {
            if(this.repoItinerario.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.SEGNALATO))
                throw new IllegalArgumentException("L'itinerario è già stato segnalato");
            throw new IllegalArgumentException("Il punto non esiste. Controlla di aver scritto bene le caratteristiche");
        }
        Itinerario itinerario = this.repoItinerario.findItinerarioByTitoloAndComune(nomeContenuto, comune);
        itinerario.setStato(StatoContenuto.SEGNALATO);
        this.repoItinerario.save(itinerario);
    }

    public void AccettaORifiuta(String nomeContenuto, String comune, StatoContenuto stato) {
        if(!this.repoItinerario.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.ATTESA))
            throw new IllegalArgumentException("Il punto non è presente tra le richieste. " +
                    "Si prega di controllare di aver scritto bene il nome e riprovare");
        Itinerario itinerario = this.repoItinerario.findItinerarioByTitoloAndComune(nomeContenuto,comune);
        if(stato == StatoContenuto.RIFIUTATO)
            this.repoItinerario.delete(itinerario);
        else {
            itinerario.setStato(stato);
            this.repoItinerario.save(itinerario);
            List<Itinerario> itinerari = this.repoItinerario.findByComuneAndStato(comune, StatoContenuto.ATTESA);
            if(!itinerari.isEmpty())
                EliminaDoppioni(itinerari, itinerario);
        }
    }

    private void EliminaDoppioni(List<Itinerario> itinerari, Itinerario itinerario) {
        for(Itinerario itinerarioTrovato : itinerari) {
            if(itinerario.getPuntiDiInteresse().containsAll(itinerarioTrovato.getPuntiDiInteresse()) &&
                    itinerario.getPuntiDiInteresse().size() == itinerarioTrovato.getPuntiDiInteresse().size())
                this.repoItinerario.delete(itinerarioTrovato);
        }
    }

    public void EliminaItinerariCheSiRipetonoPerNomeOPunti(Itinerario itinerario) {
        List<Itinerario> itinerari = this.repoItinerario.findByComuneAndStato(itinerario.getComune(), StatoContenuto.ATTESA);
        if(itinerari != null){
            for(Itinerario itinerarioTrovato : itinerari) {
                if(itinerario.getPuntiDiInteresse().containsAll(itinerarioTrovato.getPuntiDiInteresse()) &&
                        itinerario.getPuntiDiInteresse().size() == itinerarioTrovato.getPuntiDiInteresse().size())
                    this.repoItinerario.delete(itinerarioTrovato);
                if(itinerario.getTitolo().equals(itinerarioTrovato.getTitolo()))
                    this.repoItinerario.delete(itinerarioTrovato);
            }
        }


    }

    public void ControllaPresenzaNome(String titolo, String comune) {
        Itinerario itineraro = this.repoItinerario.findItinerarioByTitoloAndComune(titolo, comune);
        if(itineraro != null)
            throw new IllegalArgumentException("Esiste già un itinerario (accettato, in attesa o segnalato) con questo nome. " +
                    "Prova ad essere più specifico/a");
    }

    public List<ItinerarioResponseDTO> GetItinerariPreferiti(Long idUtente, String nomeComune) {
        List<Itinerario> itinerari = this.repoItinerario.findByComuneAndStato(nomeComune, StatoContenuto.APPROVATO);
        List<ItinerarioResponseDTO> itinerariPreferiti = new ArrayList<>();
        for(Itinerario itinerario: itinerari){
            if(itinerario.getIdUtenteContenutoPreferito().contains(idUtente)) {
                ItinerarioResponseDTO preferito = new ItinerarioResponseDTO(itinerario.getTitolo(),
                        itinerario.getDescrizione(), itinerario.getAutore().getUsername());
                preferito.setLuoghi(ConvertiInListaDiLuoghiDTO(itinerario.getPuntiDiInteresse()));
                itinerariPreferiti.add(preferito);
            }
        }
        return itinerariPreferiti;
    }

    public List<ItinerarioResponseDTO> GetItinerarioByAutore(User autore) {
        List<Itinerario> itinerari = this.repoItinerario.findByAutore(autore);
        List<ItinerarioResponseDTO> itinerariPropri = new ArrayList<>();
        if(itinerari != null){
            for(Itinerario itinerario: itinerari){
                ItinerarioResponseDTO nuovo = new ItinerarioResponseDTO(itinerario.getTitolo(), itinerario.getDescrizione(),
                        itinerario.getAutore().getUsername());
                nuovo.setLuoghi(ConvertiInListaDiLuoghiDTO(itinerario.getPuntiDiInteresse()));
                itinerariPropri.add(nuovo);
            }
        }
        return itinerariPropri;
    }

    public void EliminaItinerario(String nomeContenuto, String comune) {
        Itinerario itinerario = this.repoItinerario.findItinerarioByTitoloAndComune(nomeContenuto, comune);
        if(itinerario == null)
            throw new NullPointerException("L'itinerario non esiste. Controllare di aver inserito correttamente il titolo");
        this.repoItinerario.delete(itinerario);
    }
}
