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
import java.util.Locale;

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

    public void AggiungiContenuto(Evento contenuto) {
        this.repoEvento.save(contenuto);
    }

    public void ApprovaContenuto(long id, Evento contenuto, StatoContenuto nuovoStato) {
        User user = repoUtente.getById(id);
        if (nuovoStato == StatoContenuto.APPROVATO) {
            contenuto.setStato(nuovoStato);
            repoEvento.save(contenuto);
        } else {
            repoEvento.delete(contenuto);
        }
    }

    public Evento GetEventoByNome(String nome){
        return this.repoEvento.findEventoByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public List<EventoResponseDTO> GetEventiStatoByComune(String comune) {
        List<Evento> eventiPresenti = this.repoEvento.findEventoByComune(comune);
        List<EventoResponseDTO> eventi = new ArrayList<>();
        for (Evento evento : eventiPresenti) {
            if (!(evento.getStato() == StatoContenuto.ATTESA)) {
                EventoResponseDTO nuovo = new EventoResponseDTO(evento.getTitolo(),
                        evento.getDescrizione(), evento.getDurata(), evento.getAutore().getUsername());
                nuovo.setLuogo(ConvertiInLuogoDTO(evento.getLuogo()));
                eventi.add(nuovo);
            }
        }
        return eventi;
    }

    private LuogoDTO ConvertiInLuogoDTO(PuntoGeolocalizzato riferimento) {
        return new LuogoDTO(riferimento.getTitolo(), riferimento.getLatitudine(), riferimento.getLongitudine());
    }

    public void AggiungiPreferito(String nomeContenuto, Long idUtente) {
        Evento evento = this.repoEvento.findEventoByTitolo(nomeContenuto);
        evento.getIdUtenteContenutoPreferito().add(idUtente);
        this.repoEvento.save(evento);
    }

    public List<EventoResponseDTO> GetEventiStatoByComune(String comune, StatoContenuto stato) {
        List<Evento> eventiPresenti = this.repoEvento.findEventoByComune(comune);
        List<EventoResponseDTO> eventi = new ArrayList<>();
        for (Evento evento : eventiPresenti) {
            if (evento.getStato() == stato) {
                EventoResponseDTO nuovo = new EventoResponseDTO(evento.getTitolo(),
                        evento.getDescrizione(), evento.getDurata(), evento.getAutore().getUsername());
                nuovo.setLuogo(ConvertiInLuogoDTO(evento.getLuogo()));
                eventi.add(nuovo);
            }
        }
        return eventi;
    }

    public void AccettaORifiuta(String nomeContenuto, Long idUtente, StatoContenuto stato) {
        Evento evento = this.repoEvento.findEventoByTitolo(nomeContenuto);
        if(stato == StatoContenuto.RIFIUTATO)
            this.repoEvento.delete(evento);
        else{
            evento.setStato(stato);
            this.repoEvento.save(evento);
        }
    }
}
