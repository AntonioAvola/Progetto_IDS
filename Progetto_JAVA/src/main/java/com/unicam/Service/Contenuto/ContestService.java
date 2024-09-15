package com.unicam.Service.Contenuto;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.ContestRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.dto.Risposte.ContestResponseDTO;
import com.unicam.dto.Risposte.ContestVotiDTO;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

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
                if(contest.getDurata().getFine().isAfter(adesso) && contest.getDurata().getInizio().isBefore(adesso))
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
                contests.add(new ContestResponseDTO(contest.getTitolo(), contest.getDescrizione(),
                        contest.getDurata().getFine(), contest.getAutore().getUsername()));
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

    public void PartecipaContest(String titolo, String comune, boolean partecipo, long idUtente) {
        Contest contest = this.repoContest.findContestByTitoloAndComune(titolo, comune);
        if(contest.getIdPartecipanti().contains(idUtente))
            throw new IllegalArgumentException("Hai già partecipato a questo contest");
        contest.getIdPartecipanti().add(idUtente);
        if(partecipo)
            contest.setVotiFavore(contest.getVotiFavore()+1);
        else
            contest.setVotiContrari(contest.getVotiContrari()+1);
        this.repoContest.save(contest);
    }

    public void ControllaPresenzaNomeApprovato(String titolo, String comune) {
        if(!this.repoContest.existsByTitoloAndComuneAndStato(titolo, comune, StatoContenuto.APPROVATO))
            throw new IllegalArgumentException("Il contest non è ancora stato approvato. " +
                    "Assicurarsi di aver inserito correttamente il nome del contest");
    }

    public List<ContestVotiDTO> GetContestByComuneTempo(String comune, LocalDateTime adesso, boolean finito) {
        List<Contest> contests = this.repoContest.findByComuneAndStato(comune, StatoContenuto.APPROVATO);
        List<ContestVotiDTO> restituiti = new ArrayList<>();
        for(Contest contest: contests){
            if(finito == true) {
                if (contest.getDurata().getFine().isBefore(adesso))
                    restituiti.add(new ContestVotiDTO(contest.getTitolo(), contest.getDescrizione(),
                            contest.getDurata().getFine(), contest.getVotiFavore(), contest.getVotiContrari()));
            }
            else {
                if (contest.getDurata().getFine().isAfter(adesso) && contest.getDurata().getInizio().isBefore(adesso))
                    restituiti.add(new ContestVotiDTO(contest.getTitolo(), contest.getDescrizione(),
                            contest.getDurata().getFine(), contest.getVotiFavore(), contest.getVotiContrari()));
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
}
