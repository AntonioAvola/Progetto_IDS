package com.unicam.Service.Contenuto;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.ContestRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.dto.Risposte.ContestResponseDTO;
import org.springframework.stereotype.Service;

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

    public void ApprovaContenuto(long id, Contest contenuto, StatoContenuto nuovoStato) {
        User user = repoUtente.getById(id);
        if (nuovoStato == StatoContenuto.APPROVATO) {
            contenuto.setStato(nuovoStato);
            repoContest.save(contenuto);
        } else {
            repoContest.delete(contenuto);
        }
    }

    public Contest GetIContestByNome(String nome){
        return this.repoContest.findContestByTitolo(nome.toUpperCase(Locale.ROOT));
    }

    public List<ContestResponseDTO> GetContestByComuneRuolo(String comune, Ruolo role) {
        List<Contest> contestPresenti = this.repoContest.findContestByComune(comune);
        List<ContestResponseDTO> contests = new ArrayList<>();
        for (Contest contest : contestPresenti) {
            if (contest.getStato() != StatoContenuto.ATTESA && contest.getPartecipanti().contains(role)) {
                contests.add(new ContestResponseDTO(contest.getTitolo(), contest.getDescrizione(),
                        contest.getDurata(), contest.getAutore().getUsername()));
            }
        }
        return contests;
    }

    public void AggiungiPreferito(String nomeContenuto, String comune, Long idUtente) {
        if(!this.repoContest.existsByTitoloAndComuneAndStato(nomeContenuto, comune, StatoContenuto.APPROVATO))
            throw new IllegalArgumentException("Il punto non esiste. Controlla di aver scritto bene le caratteristiche");
        Contest contest = this.repoContest.findContestByTitolo(nomeContenuto);
        List<Long> utentePreferito = contest.getIdUtenteContenutoPreferito();
        if(utentePreferito.contains(idUtente))
            throw new IllegalArgumentException("Il contest è già tra i preferiti");
        contest.getIdUtenteContenutoPreferito().add(idUtente);
        this.repoContest.save(contest);
    }

    public List<ContestResponseDTO> GetContestStatoByComune(String comune, StatoContenuto stato) {
        List<Contest> contestPresenti = this.repoContest.findContestByComune(comune);
        List<ContestResponseDTO> contests = new ArrayList<>();
        for (Contest contest : contestPresenti) {
            if (contest.getStato() == stato) {
                contests.add(new ContestResponseDTO(contest.getTitolo(), contest.getDescrizione(),
                        contest.getDurata(), contest.getAutore().getUsername()));
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
        if(this.repoContest.existsByTitoloAndComune(titolo, comune))
            throw new IllegalArgumentException("Esiste già un contest con questo titolo. Si prega di cambiarlo");
    }
}
