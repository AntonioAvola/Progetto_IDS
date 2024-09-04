package com.unicam.Service;

import com.unicam.Authorization.AuthorizationService;
import com.unicam.Model.*;
import com.unicam.Repository.IContenutoRepository;
import com.unicam.Repository.IUtenteRepository;
import com.unicam.dto.Provvisori.SegnalazioneProvvisoriaDTO;
import com.unicam.dto.Risposte.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class ContenutoService <T extends Contenuto> {

    private final IContenutoRepository<T> repo;

    private final IUtenteRepository repoUser;
    private AuthorizationService autorizzazioni = new AuthorizationService();


    @Autowired
    public ContenutoService(IContenutoRepository<T> repo, IUtenteRepository repoUser) {
        this.repoUser = repoUser;
        this.repo = repo;
    }

    public void AggiungiContenuto(T contenuto){
        repo.save(contenuto);
    }

    public void ApprovaContenuto(Long id, T contenuto, StatoContenuto nuovoStato){
        RicercaUtente(id);
        User user = repoUser.getById(id);
        VerificaAutorizzazioni(user);
        if(nuovoStato == StatoContenuto.APPROVATO){
            contenuto.setStato(nuovoStato);
            repo.save(contenuto);
        }
        else{
            repo.delete(contenuto);
        }
    }

    private void VerificaAutorizzazioni(User user) {
        if(!autorizzazioni.VerificaPermesso(user, "AggiungiContenuto")){
            throw new UnsupportedOperationException("Non hai il permesso di creare contenuti");
        }
    }

    private void RicercaUtente(Long id) {
        if(!repo.existsById(id)){
            throw new IllegalArgumentException("L'utente non esiste");
        }
    }

    public List<PuntoGeolocalizzato> GetPuntiByListaNomi(List<String> nomiPunti) {
        List<PuntoGeolocalizzato> punti = new ArrayList<>();
        for (String nome: nomiPunti) {
            punti.add(GetPuntoByNome(nome.toUpperCase(Locale.ROOT)));
        }
        return punti;
    }

    public PuntoGeolocalizzato GetPuntoByNome(String nome){
        return this.repo.findByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public void SegnalaContenuto(SegnalazioneProvvisoriaDTO segnala) {
        this.repo.findByTitolo(segnala.getNomeContenuto()).setStato(StatoContenuto.SEGNALATO);
    }

    public List<PuntoGeoResponseDTO> GetPuntiGeoByComune(String comune) {
        List<PuntoGeolocalizzato> puntiPresenti = this.repo.findPuntoGeoByComune(comune);
        List<PuntoGeoResponseDTO> punti = new ArrayList<>();
        for (PuntoGeolocalizzato punto: puntiPresenti) {
            if(!(punto.getStato() == StatoContenuto.ATTESA))
                punti.add(new PuntoGeoResponseDTO(punto.getTitolo(), punto.getDescrizione(),
                        punto.getLatitudine(), punto.getLongitudine(), punto.getAutore().getUsername()));
        }
        return punti;
    }

    public List<PuntoLogicoResponseDTO> GetPuntiLogiciByComune(String comune) {
        List<PuntoLogico> puntiPresenti = this.repo.findPuntoLogicoByComune(comune);
        List<PuntoLogicoResponseDTO> punti = new ArrayList<>();
        for (PuntoLogico punto: puntiPresenti) {
            if(!(punto.getStato() == StatoContenuto.ATTESA)){
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

    public List<ItinerarioResponseDTO> GetItinerariByComune(String comune) {
        List<Itinerario> itinerariPresenti = this.repo.findItinerarioByComune(comune);
        List<ItinerarioResponseDTO> itinerari = new ArrayList<>();
        for (Itinerario itinerario: itinerariPresenti) {
            if(!(itinerario.getStato() == StatoContenuto.ATTESA)){
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
        for (PuntoGeolocalizzato punto: puntiDiInteresse) {
            luoghi.add(ConvertiInLuogoDTO(punto));
        }
        return luoghi;
    }

    public List<EventoResponseDTO> GetEventiByComune(String comune) {
        List<Evento> eventiPresenti = this.repo.findEventoByComune(comune);
        List<EventoResponseDTO> eventi = new ArrayList<>();
        for (Evento evento: eventiPresenti) {
            if(!(evento.getStato() == StatoContenuto.ATTESA)){
                EventoResponseDTO nuovo = new EventoResponseDTO(evento.getTitolo(),
                        evento.getDescrizione(), evento.getDurata(), evento.getAutore().getUsername());
                nuovo.setLuogo(ConvertiInLuogoDTO(evento.getLuogo()));
                eventi.add(nuovo);
            }
        }
        return eventi;
    }

    public List<ContestResponseDTO> GetContestByComuneRuolo(String comune, Ruolo role) {
        List<Contest> contestPresenti = this.repo.findContestByComune(comune);
        List<ContestResponseDTO> contests = new ArrayList<>();
        for (Contest contest: contestPresenti) {
            if(contest.getStato() != StatoContenuto.ATTESA && contest.getPartecipanti().contains(role)){
                contests.add(new ContestResponseDTO(contest.getTitolo(), contest.getDescrizione(),
                        contest.getDurata(), contest.getAutore().getUsername()));
            }
        }
        return contests;
    }
}
