package com.unicam.Service.Contenuto;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.EventoRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.dto.Risposte.EventoResponseDTO;
import com.unicam.dto.Risposte.LuogoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EventoService {

    private final EventoRepository repoEvento;
    private final UtenteRepository repoUtente;

    @Autowired
    public EventoService(EventoRepository repoPunto,
                              UtenteRepository repoUtente){
        this.repoEvento = repoPunto;
        this.repoUtente = repoUtente;
    }

    /**
     * Inserimento di un evento. Il nuovo evento viene salvato nel database solo se non esistono
     * eventi approvati nello stesso luogo. Nel caso sia presente nel database un evento con lo
     * stesso luogo, l'evento viene salvato nel database solo se entrambi sono in stato di attesa.
     * La presenza di contenuti ripetuti in attesa verrà gestita al momento dell'approvazione di uno
     * degli oggetti presenti più volte.
     *
     * @param contenuto        evento da inserire nel database
     * @exception IllegalArgumentException se è gia stato approvato un evento con lo stesso luogo
     *                                       dell'evento che si sta provando ad aggiungere. L'eccezione
     *                                       viene scatenata se si accavallano gli orari di inizio e/o fine
     */
    public void AggiungiContenuto(Evento contenuto) {
        List<Evento> eventiPresenti = this.repoEvento.findEventoByComune(contenuto.getComune());
        for(Evento eventoTrovato : eventiPresenti){
            if(eventoTrovato.getStato() == StatoContenuto.APPROVATO && eventoTrovato.getLuogo().equals(contenuto.getLuogo())){
                if((contenuto.getDurata().getInizio().isBefore(eventoTrovato.getDurata().getFine())
                        && contenuto.getDurata().getInizio().isAfter(eventoTrovato.getDurata().getInizio())))
                    throw new IllegalArgumentException("Non è possibile proporre l'evento, dato che è gia stato approvato " +
                            "un evento che inizia il "+ eventoTrovato.getDurata().getInizio().getDayOfMonth());
                if(contenuto.getDurata().getFine().isAfter(eventoTrovato.getDurata().getInizio())
                        && contenuto.getDurata().getFine().isBefore(eventoTrovato.getDurata().getFine()))
                    throw new IllegalArgumentException("Non è possibile proporre l'evento, dato che è gia stato approvato " +
                            "un evento che finisce il "+ eventoTrovato.getDurata().getFine().getDayOfMonth());
                if(contenuto.getDurata().getInizio().isBefore(eventoTrovato.getDurata().getInizio())
                        && contenuto.getDurata().getFine().isAfter(eventoTrovato.getDurata().getFine()))
                    throw new IllegalArgumentException("Non è possibile proporre l'evento, dato che è gia stato approvato " +
                            "un evento che inizia il "+ eventoTrovato.getDurata().getInizio().getDayOfMonth() + " e finisce il " +
                            eventoTrovato.getDurata().getFine().getDayOfMonth());
            }
        }
        this.repoEvento.save(contenuto);
    }

    public List<EventoResponseDTO> GetEventiByComune(String comune) {
        List<Evento> eventiPresenti = this.repoEvento.findEventoByComune(comune);
        List<EventoResponseDTO> eventi = new ArrayList<>();
        for (Evento evento : eventiPresenti) {
            if (evento.getStato() == StatoContenuto.APPROVATO) {
                EventoResponseDTO nuovo = new EventoResponseDTO(evento.getTitolo(),
                        evento.getDescrizione(), evento.getDurata().getInizio(),
                        evento.getDurata().getFine(), evento.getAutore().getUsername());
                nuovo.setLuogo(ConvertiInLuogoDTO(evento.getLuogo()));
                eventi.add(nuovo);
            }
        }
        return eventi;
    }

    private LuogoDTO ConvertiInLuogoDTO(PuntoGeolocalizzato riferimento) {
        return new LuogoDTO(riferimento.getTitolo(), riferimento.getLatitudine(), riferimento.getLongitudine());
    }

    public void AggiungiPreferito(String nomeContenuto, String comune, Long idUtente) {
        if(!this.repoEvento.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.APPROVATO))
            throw new IllegalArgumentException("Il punto non esiste. Controlla di aver scritto bene le caratteristiche");
        Evento evento = this.repoEvento.findEventoByTitoloAndComune(nomeContenuto, comune);
        List<Long> utentePreferito = evento.getIdUtenteContenutoPreferito();
        if(utentePreferito.contains(idUtente))
            throw new IllegalArgumentException("L'evento è già tra i preferiti");
        evento.getIdUtenteContenutoPreferito().add(idUtente);
        this.repoEvento.save(evento);
    }

    public List<EventoResponseDTO> GetEventiByComune(String comune, StatoContenuto stato) {
        List<Evento> eventiPresenti = this.repoEvento.findEventoByComune(comune);
        List<EventoResponseDTO> eventi = new ArrayList<>();
        for (Evento evento : eventiPresenti) {
            if (evento.getStato() == stato) {
                EventoResponseDTO nuovo = new EventoResponseDTO(evento.getTitolo(),
                        evento.getDescrizione(), evento.getDurata().getInizio(), evento.getDurata().getFine(),
                        evento.getAutore().getUsername());
                nuovo.setLuogo(ConvertiInLuogoDTO(evento.getLuogo()));
                eventi.add(nuovo);
            }
        }
        return eventi;
    }

    public void AccettaORifiuta(String nomeContenuto, String comune, StatoContenuto stato) {
        if(!this.repoEvento.existsByTitoloAndComune(nomeContenuto, comune))
            throw new IllegalArgumentException("L'evento non è presente tra le richieste. " +
                    "Si prega di controllare di aver scritto bene il nome e riprovare");
        Evento evento = this.repoEvento.findEventoByTitoloAndComune(nomeContenuto, comune);
        if(stato == StatoContenuto.RIFIUTATO)
            this.repoEvento.delete(evento);
        else{
            evento.setStato(stato);
            this.repoEvento.save(evento);
            List<Evento> eventiAttesa = this.repoEvento.findEventoByComuneAndStato(evento.getComune(), StatoContenuto.ATTESA);
            if(!eventiAttesa.isEmpty())
                EliminaProposteEventiCheSiSovrappongono(eventiAttesa, evento);
        }
    }

    public void EliminaProposteEventiCheSiSovrappongono(List<Evento> eventiAttesa, Evento evento) {
        for(Evento eventoTrovato : eventiAttesa){
            if(eventoTrovato.getLuogo().equals(evento.getLuogo())){
                if((eventoTrovato.getDurata().getInizio().isBefore(evento.getDurata().getFine())
                        && eventoTrovato.getDurata().getInizio().isAfter(evento.getDurata().getInizio())))
                    this.repoEvento.delete(eventoTrovato);
                if(eventoTrovato.getDurata().getFine().isAfter(evento.getDurata().getInizio())
                        && eventoTrovato.getDurata().getFine().isBefore(evento.getDurata().getFine()))
                    this.repoEvento.delete(eventoTrovato);
                if(eventoTrovato.getDurata().getInizio().isBefore(evento.getDurata().getInizio())
                        && eventoTrovato.getDurata().getFine().isAfter(evento.getDurata().getFine()))
                    this.repoEvento.delete(eventoTrovato);
                if(eventoTrovato.getDurata().getInizio().isEqual(evento.getDurata().getInizio())
                        || eventoTrovato.getDurata().getFine().isEqual(evento.getDurata().getFine()))
                    this.repoEvento.delete(eventoTrovato);
            }
        }
    }

    public void ControllaPresenzaNome(String titolo, String comune) {
        if(this.repoEvento.existsByTitoloAndComune(titolo, comune))
            throw new IllegalArgumentException("Esiste già un evento (accettato o in attesa) con questo nome. " +
                    "Si prega di cambiarlo o essere più specifico/a");
    }

    public List<EventoResponseDTO> GetEventiPreferiti(Long idUtente, String nomeComune) {
        List<Evento> eventi = this.repoEvento.findEventoByComuneAndStato(nomeComune, StatoContenuto.APPROVATO);
        List<EventoResponseDTO> eventiPreferiti = new ArrayList<>();
        for(Evento evento: eventi){
            if(evento.getIdUtenteContenutoPreferito().contains(idUtente))
                eventiPreferiti.add(new EventoResponseDTO(evento.getTitolo(), evento.getDescrizione(),
                        evento.getDurata().getInizio(), evento.getDurata().getFine(),
                        evento.getAutore().getUsername()));
        }
        return eventiPreferiti;
    }

    public List<EventoResponseDTO> GetEventiByAutore(User autore) {
        List<Evento> eventi = this.repoEvento.findByAutore(autore);
        List<EventoResponseDTO> eventiPropri = new ArrayList<>();
        if(eventi != null){
            for(Evento evento: eventi){
                EventoResponseDTO nuovo = new EventoResponseDTO(evento.getTitolo(), evento.getDescrizione(),
                        evento.getDurata().getInizio(), evento.getDurata().getFine(), evento.getAutore().getUsername());
                nuovo.setLuogo(ConvertiInLuogoDTO(evento.getLuogo()));
                eventiPropri.add(nuovo);
            }
        }
        return eventiPropri;
    }

    public void EliminaEvento(String nomeContenuto, String comune) {
        Evento evento = this.repoEvento.findEventoByTitoloAndComune(nomeContenuto, comune);
        if(evento == null)
            throw new NullPointerException("L'evento non esiste. Controllare di aver inserito correttamente il nome dell'evento");
        this.repoEvento.delete(evento);
    }
}
