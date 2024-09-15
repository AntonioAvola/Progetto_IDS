package com.unicam.Service.Contenuto;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.ContestRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.dto.ContestTerminatoDTO;
import com.unicam.dto.EsitoContestDTO;
import com.unicam.dto.Risposte.ContestResponseDTO;
import com.unicam.dto.Risposte.ContestVotiDTO;
import com.unicam.dto.Risposte.VincitoriContestDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ContestService {

    private final ContestRepository repoContest;
    private final UtenteRepository repoUtente;

    public ContestService(ContestRepository repoPunto,
                              UtenteRepository repoUtente){
        this.repoContest = repoPunto;
        this.repoUtente = repoUtente;
    }

    /**
     * Inserimento di un contest. Il contest viene salvato nel database solo se
     * non ci sono altri contest salvati nel database con lo stesso nome.
     * Possono essere presenti più contest nello stesso arco di tempo o accavallati
     * gli uni con gli altri.
     *
     * @param contenuto        contest da inserire nel database
     * @exception IllegalArgumentException se è gia presente un contest con lo stesso nome
     */
    public void AggiungiContenuto(Contest contenuto) {
        this.repoContest.save(contenuto);
    }

    public List<ContestResponseDTO> GetContestByComuneRuolo(String comune, LocalDateTime adesso) {
        List<Contest> contestPresenti = this.repoContest.findContestByComune(comune);
        List<ContestResponseDTO> contests = new ArrayList<>();
        for (Contest contest : contestPresenti) {
            if (contest.getStato() == StatoContenuto.APPROVATO) {
                if(contest.getDurata().getFine().isAfter(adesso))
                    contests.add(new ContestResponseDTO(contest.getTitolo(), contest.getDescrizione(),
                            contest.getDurata().getFine(), contest.getAutore().getUsername()));
            }
        }
        return contests;
    }

    public List<ContestResponseDTO> GetContestStatoByComune(String comune, StatoContenuto stato, LocalDateTime adesso) {
        List<Contest> contestPresenti = this.repoContest.findContestByComune(comune);
        List<ContestResponseDTO> contests = new ArrayList<>();
        for (Contest contest : contestPresenti) {
            if (contest.getStato() == stato) {
                if(contest.getDurata().getFine().isBefore(adesso)){
                    this.repoContest.delete(contest);
                }
                else {
                    contests.add(new ContestResponseDTO(contest.getTitolo(), contest.getDescrizione(),
                            contest.getDurata().getFine(), contest.getAutore().getUsername()));
                }
            }
        }
        return contests;
    }

    public void AccettaORifiuta(String nomeContenuto, String comune, StatoContenuto stato) {
        if(!this.repoContest.existsByTitoloAndComune(nomeContenuto, comune))
            throw new IllegalArgumentException("Il contest non è presente tra le richieste. " +
                    "Si prega di controllare di aver inserito correttamente il nome e riprovare");
        Contest contest = this.repoContest.findContestByTitoloAndComune(nomeContenuto, comune);
        if(stato == StatoContenuto.RIFIUTATO)
            this.repoContest.delete(contest);
        else{
            contest.setStato(stato);
            this.repoContest.save(contest);
        }
    }

    public void ControllaPresenzaNome(String titolo, String comune) {
        if(this.repoContest.existsByTitoloAndComuneAndStato(titolo, comune, StatoContenuto.APPROVATO)){
            throw new IllegalArgumentException("Esiste già un contest approvato con questo titolo. Rimonimare il contest");
        }
        if(this.repoContest.existsByTitoloAndComuneAndStato(titolo, comune, StatoContenuto.ATTESA))
            throw new IllegalArgumentException("Esiste già un contest in attesa con questo titolo. Si prega di cambiarlo");
    }

    public void PartecipaContest(String titolo, String comune, long idUtente, LocalDateTime adesso) {
        Contest contest = this.repoContest.findContestByTitoloAndComune(titolo, comune);
        if(contest.getDurata().getFine().isBefore(adesso)){
            throw new IllegalArgumentException("Il contest è già terminato. Non si può più partecipare");
        }
        if(contest.getDurata().getInizio().isAfter(adesso)){
            throw new IllegalArgumentException("Il contest deve ancora iniziare");
        }
        if(contest.getListaPartecipanti().contains(repoUtente.findUserById(idUtente).getUsername()))
            throw new IllegalArgumentException("Hai già partecipato a questo contest");
        contest.getListaPartecipanti().add(repoUtente.findUserById(idUtente).getUsername());
        this.repoContest.save(contest);
    }

    public void ControllaPresenzaNomeApprovato(String titolo, String comune) {
        if(!this.repoContest.existsByTitoloAndComuneAndStato(titolo, comune, StatoContenuto.APPROVATO))
            throw new IllegalArgumentException("Il contest non è ancora stato approvato. " +
                    "Assicurarsi di aver inserito correttamente il nome del contest");
    }

    public List<ContestVotiDTO> GetContestByComuneTempo(String comune, LocalDateTime adesso) {
        List<Contest> contests = this.repoContest.findByComuneAndStato(comune, StatoContenuto.APPROVATO);
        List<ContestVotiDTO> restituiti = new ArrayList<>();
        for(Contest contest: contests){
            if (contest.getDurata().getFine().isBefore(adesso)){
                if(contest.getVincitore().equals("")){
                    restituiti.add(new ContestVotiDTO(contest.getTitolo(), contest.getDescrizione(),
                            contest.getDurata().getFine(), "--- da nominare ---"));
                }
            }
        }
        return restituiti;
    }

    public List<ContestResponseDTO> GetContestByAutore(User autore) {
        List<Contest> contests = this.repoContest.findByAutore(autore);
        List<ContestResponseDTO> contestPropri = new ArrayList<>();
        if(contests != null){
            for(Contest contest: contests){
                contestPropri.add(new ContestResponseDTO(contest.getTitolo(), contest.getDescrizione(),
                        contest.getDurata().getFine(), contest.getAutore().getUsername()));
            }
        }
        return contestPropri;
    }


    public void EliminaContest(String nomeContenuto, String comune) {
        Contest contest = this.repoContest.findContestByTitoloAndComune(nomeContenuto, comune);
        if(contest == null)
            throw new NullPointerException("Il contest non esiste. Controllare di aver inserito correttamente il titolo del contest");
        this.repoContest.delete(contest);
    }

    public void AssegnaVincitore(String contest, String vincitore, LocalDateTime adesso, String comune) {
        Contest contestTrovato = this.repoContest.findContestByTitoloAndComune(contest, comune);
        if(contestTrovato.getDurata().getFine().isAfter(adesso)){
            throw new IllegalArgumentException("Il contest non è ancora terminato");
        }
        if(!contestTrovato.getListaPartecipanti().contains(vincitore)){
            throw new IllegalArgumentException("Utente non trovato tra i partecipanti. Controllare di aver scritto bene l'username del vincitore");
        }
        if(!contestTrovato.getVincitore().equals("")){
            throw new IllegalArgumentException("Il contest selezionato ha già un vincitore assegnato");
        }
        contestTrovato.setVincitore(vincitore);
        this.repoContest.save(contestTrovato);
    }

    public ContestTerminatoDTO PartecipantiContest(String contest, String comune, LocalDateTime adesso) {
        Contest contestTrovato = this.repoContest.findContestByTitoloAndComune(contest, comune);
        if(contestTrovato.getDurata().getFine().isAfter(adesso)){
            throw new IllegalArgumentException("Il contest non è ancora terminato");
        }
        ContestTerminatoDTO contestPartecipanti = new ContestTerminatoDTO(contestTrovato.getTitolo(), contestTrovato.getListaPartecipanti());
        return contestPartecipanti;
    }

    public List<EsitoContestDTO> EsitoContest(String username, LocalDateTime adesso) {
        List<Contest> contest = this.repoContest.findAll();
        List<EsitoContestDTO> esiti = new ArrayList<>();
        for(Contest contestTrovato : contest){
            if(contestTrovato.getListaPartecipanti().contains(username)){
                EsitoContestDTO esito = new EsitoContestDTO(contestTrovato.getComune(), contestTrovato.getTitolo(), contestTrovato.getDescrizione());
                if(contestTrovato.getVincitore().equals(username)){
                    esito.setEsito("VINCITORE");
                }
                else if(contestTrovato.getDurata().getFine().isAfter(adesso)){
                    esito.setEsito("---- contest ancora in corso ----");
                }
                else if(contestTrovato.getVincitore().equals("")){
                    esito.setEsito("---- vincitore ancora da proclamare ----");
                }
                else {
                    esito.setEsito("Non hai vinto");
                }
                esiti.add(esito);
            }
        }
        return esiti;
    }

    public List<VincitoriContestDTO> VincitoriContest(String comune) {
        List<Contest> contests = this.repoContest.findContestByComune(comune);
        List<VincitoriContestDTO> vincitori = new ArrayList<>();
        for(Contest contest: contests){
            if(!contest.getVincitore().equals("")){
                vincitori.add(new VincitoriContestDTO(contest.getTitolo(), contest.getVincitore()));
            }
        }
        return vincitori;
    }
}
