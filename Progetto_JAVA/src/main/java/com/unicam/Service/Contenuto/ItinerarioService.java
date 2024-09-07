package com.unicam.Service.Contenuto;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.ItinerarioRepository;
import com.unicam.Repository.Contenuto.PuntoGeoRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.dto.Risposte.ItinerarioResponseDTO;
import com.unicam.dto.Risposte.LuogoDTO;
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
        //TODO controllare che i punti selezionati siano approvati
        List<Itinerario> itinerari = ItinerariApprovati(contenuto.getComune());
        for (Itinerario itinerario : itinerari) {
            if(itinerario.getPuntiDiInteresse().containsAll(contenuto.getPuntiDiInteresse())
                    && itinerario.getPuntiDiInteresse().size() == contenuto.getPuntiDiInteresse().size())
                throw new IllegalArgumentException("Esiste già un itinerario con questi punti di interesse");
            if(itinerario.getTitolo().equals(contenuto.getTitolo()))
                throw new IllegalArgumentException("Esiste già un itinerario con questo nome. Si presa di cambiarlo");
        }
        this.repoItinerario.save(contenuto);
    }

    private List<Itinerario> ItinerariApprovati(String comune) {
        List<Itinerario> itinerari = new ArrayList<>();
        List<Itinerario> presenti = this.repoItinerario.findItinerarioByComune(comune);
        if(presenti != null){
            for (Itinerario itinerario: presenti) {
                if(!(itinerario.getStato() == StatoContenuto.ATTESA))
                    itinerari.add(itinerario);
            }
        }
        return itinerari;
    }

    public List<PuntoGeolocalizzato> GetPuntiByListaNomi(List<String> nomiPunti) {
        List<PuntoGeolocalizzato> punti = new ArrayList<>();
        for (String nome : nomiPunti) {
            punti.add(GetPuntoGeoByNome(nome.toUpperCase(Locale.ROOT)));
        }
        return punti;
    }

    public PuntoGeolocalizzato GetPuntoGeoByNome(String nome) {
        return this.repoPunto.findGeoByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public void ApprovaContenuto(long id, Itinerario contenuto, StatoContenuto nuovoStato) {
        User user = repoUtente.getById(id);
        if (nuovoStato == StatoContenuto.APPROVATO) {
            contenuto.setStato(nuovoStato);
            repoItinerario.save(contenuto);
        } else {
            repoItinerario.delete(contenuto);
        }
    }

    public Itinerario GetItinerarioByNome(String nome){
        return this.repoItinerario.findItinerarioByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public List<ItinerarioResponseDTO> GetItinerariByComune(String comune) {
        List<Itinerario> itinerariPresenti = this.repoItinerario.findItinerarioByComune(comune);
        List<ItinerarioResponseDTO> itinerari = new ArrayList<>();
        for (Itinerario itinerario : itinerariPresenti) {
            if (!(itinerario.getStato() == StatoContenuto.ATTESA)) {
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

    public void AggiungiPreferito(String nomeContenuto, Long idUtente) {
        Itinerario itinerario = this.repoItinerario.findItinerarioByTitolo(nomeContenuto);
        itinerario.getIdUtenteContenutoPreferito().add(idUtente);
        this.repoItinerario.save(itinerario);
    }

    public void SegnalaContenuto(String nomeContenuto, long idCreatore) {
        Itinerario itinerario = this.repoItinerario.findItinerarioByTitolo(nomeContenuto);
        itinerario.setStato(StatoContenuto.SEGNALATO);
        this.repoItinerario.save(itinerario);
    }

    public void AccettaORifiuta(String nomeContenuto, String comune, StatoContenuto stato) {
        if(!this.repoItinerario.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.ATTESA))
            throw new IllegalArgumentException("Il punto non è presente tra le richieste. " +
                    "Si prega di controllare di aver scritto bene il nome e riprovare");
        List<Itinerario> itinerari = this.repoItinerario.findItinerariByTitoloAndComune(nomeContenuto, comune);
        Itinerario itinerario = itinerari.get(0);
        itinerari.remove(itinerario);
        if(stato == StatoContenuto.RIFIUTATO)
            this.repoItinerario.delete(itinerario);
        else {
            itinerario.setStato(stato);
            this.repoItinerario.save(itinerario);
            if(!itinerari.isEmpty())
                EliminaDoppioni(itinerari, itinerario);
            }
        }

    private void EliminaDoppioni(List<Itinerario> itinerari, Itinerario itinerario) {
        for(Itinerario itinerarioTrovato : itinerari) {
            if(itinerario.getPuntiDiInteresse().containsAll(itinerarioTrovato.getPuntiDiInteresse()) &&
                    itinerario.getPuntiDiInteresse().size() == itinerarioTrovato.getPuntiDiInteresse().size())
                this.repoItinerario.delete(itinerarioTrovato);
            else if(itinerario.getTitolo().equals(itinerarioTrovato.getTitolo()))
                this.repoItinerario.delete(itinerarioTrovato);
        }
    }


    public List<PuntoGeolocalizzato> GetPuntiByListaNomiAndComune(List<String> nomiPunti, String comune) {
        List<PuntoGeolocalizzato> punti = new ArrayList<>();
        for (String punto: nomiPunti) {
            punti.add(this.repoPunto.findGeoByTitoloAndComune(punto.toUpperCase(Locale.ROOT),comune.toUpperCase(Locale.ROOT)));
        }
        return punti;
    }

    public void EliminaItinerariCheSiRipetonoPerNomeOPunti(Itinerario itinerario) {
        List<Itinerario> itinerari = this.repoItinerario.findByComuneAndStato(itinerario.getComune(), StatoContenuto.ATTESA);
        if(itinerari != null){
            for(Itinerario itinerarioTrovato : itinerari) {
                if(itinerarioTrovato.getTitolo().equals(itinerario.getTitolo()))
                    this.repoItinerario.delete(itinerarioTrovato);
                else if(itinerario.getPuntiDiInteresse().containsAll(itinerarioTrovato.getPuntiDiInteresse()) &&
                        itinerario.getPuntiDiInteresse().size() == itinerarioTrovato.getPuntiDiInteresse().size())
                    this.repoItinerario.delete(itinerarioTrovato);
            }
        }


    }
}
