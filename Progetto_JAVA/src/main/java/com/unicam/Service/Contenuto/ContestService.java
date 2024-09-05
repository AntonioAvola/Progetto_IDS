package com.unicam.Service.Contenuto;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.ContestRepository;
import com.unicam.Repository.Contenuto.PuntoLogicoRepository;
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
}
