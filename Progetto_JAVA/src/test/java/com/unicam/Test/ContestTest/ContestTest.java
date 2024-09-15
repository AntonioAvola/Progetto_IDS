package com.unicam.test.ContestTest;

import com.unicam.Model.BuilderContenuto.ContestBuilder;
import com.unicam.Model.Contest;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.Tempo;
import com.unicam.Repository.Contenuto.ContestRepository;
import com.unicam.Security.DataInitializer;
import com.unicam.Service.Contenuto.ContestService;
import com.unicam.Service.UtenteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ContestTest {

    @Autowired
    private ContestService contestService;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    private UtenteService utenteService;
    @Autowired
    private ContestRepository contestRepository;

    @Before
    public void setUp() throws Exception {
        dataInitializer.run();
    }

    @Test
    public void TestAggiuntaContest(){
        Tempo tempo = new Tempo();
        ContestBuilder contestBuilder = new ContestBuilder();
        contestBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        contestBuilder.BuildTitolo("Cerimonia di apertura");
        contestBuilder.BuildComune("ROMA");
        contestBuilder.BuildDescrizione("Cerimonia di apertura con un discorso del Sindaco");

        tempo.setInizio(LocalDateTime.of(2024, 10, 20, 9, 0));
        tempo.setFine(LocalDateTime.of(2024, 10, 20, 10, 0));
        contestBuilder.BuildSpecifica(tempo);

        Contest contest = contestBuilder.Result();
        contest.setStato(StatoContenuto.APPROVATO);

        try{
            contestService.AggiungiContenuto(contest);
            contestService.ControllaPresenzaNomeApprovato("Cerimonia di apertura", "ROMA");
        }catch (Exception e){
            fail("l'aggiunta ha restituito un errore" + e.getMessage());
        }
    }

    @Test
    public void TestAccettaContest(){
        Tempo tempo = new Tempo();
        ContestBuilder contestBuilder = new ContestBuilder();
        contestBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        contestBuilder.BuildTitolo("Cerimonia di apertura");
        contestBuilder.BuildComune("ROMA");
        contestBuilder.BuildDescrizione("Cerimonia di apertura con un discorso del Sindaco");
        tempo.setInizio(LocalDateTime.of(2024, 10, 20, 9, 0));
        tempo.setFine(LocalDateTime.of(2024, 10, 20, 10, 0));
        contestBuilder.BuildSpecifica(tempo);
        Contest contest = contestBuilder.Result();
        contest.setStato(StatoContenuto.ATTESA);

        try{
            contestService.AggiungiContenuto(contest);
            contestService.AccettaORifiuta("Cerimonia di apertura", "ROMA", StatoContenuto.APPROVATO);
            contestService.ControllaPresenzaNomeApprovato("Cerimonia di apertura", "ROMA");
        }catch (Exception e){
            fail("l'aggiunta ha restituito un errore" + e.getMessage());
        }
    }

    @Test
    public void TestRifiutaContest() {
        Tempo tempo = new Tempo();
        ContestBuilder contestBuilder = new ContestBuilder();
        contestBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        contestBuilder.BuildTitolo("Cerimonia di apertura");
        contestBuilder.BuildComune("ROMA");
        contestBuilder.BuildDescrizione("Cerimonia di apertura con un discorso del Sindaco");
        tempo.setInizio(LocalDateTime.of(2024, 10, 20, 9, 0));
        tempo.setFine(LocalDateTime.of(2024, 10, 20, 10, 0));
        contestBuilder.BuildSpecifica(tempo);
        Contest contest = contestBuilder.Result();
        contest.setStato(StatoContenuto.ATTESA);

        try{
            contestService.AggiungiContenuto(contest);
            contestService.AccettaORifiuta("Cerimonia di apertura", "ROMA", StatoContenuto.RIFIUTATO);
            assertThrows(IllegalArgumentException.class,()-> contestService.ControllaPresenzaNomeApprovato("Cerimonia di apertura", "ROMA"));
        }catch (Exception e){
            fail("l'aggiunta ha restituito un errore " + e.getMessage());
        }
    }

    @Test
    public void TestEliminaContest() {
        Tempo tempo = new Tempo();
        ContestBuilder contestBuilder = new ContestBuilder();
        contestBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        contestBuilder.BuildTitolo("Cerimonia di apertura");
        contestBuilder.BuildComune("ROMA");
        contestBuilder.BuildDescrizione("Cerimonia di apertura con un discorso del Sindaco");
        tempo.setInizio(LocalDateTime.of(2024, 10, 20, 9, 0));
        tempo.setFine(LocalDateTime.of(2024, 10, 20, 10, 0));
        contestBuilder.BuildSpecifica(tempo);
        Contest contest = contestBuilder.Result();
        contest.setStato(StatoContenuto.APPROVATO);

        try{
            contestService.AggiungiContenuto(contest);
            contestService.ControllaPresenzaNomeApprovato("Cerimonia di apertura", "ROMA");
        }catch (Exception e){
            fail("l'aggiunta ha restituito un errore " +e.getMessage());
        }

        contestService.EliminaContest("Cerimonia di apertura", "ROMA");

        try{
            assertThrows(IllegalArgumentException.class, ()-> contestService.ControllaPresenzaNomeApprovato("Cerimonia di apertura", "ROMA"));
        }catch (Exception e){
            fail("non è stata lanciata nessuna eccezione" + e.getMessage());
        }
    }



    public void TestContestAGGIUNTOConStessoNome(){
        Tempo tempo = new Tempo();
        ContestBuilder contestBuilder = new ContestBuilder();
        contestBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        contestBuilder.BuildTitolo("Concorso fotografico");
        contestBuilder.BuildComune("ROMA");
        contestBuilder.BuildDescrizione("Concorso fotografico!!");
        tempo.setInizio(LocalDateTime.of(2024, 10, 20, 9, 0));
        tempo.setFine(LocalDateTime.of(2024, 10, 20, 12, 0));
        contestBuilder.BuildSpecifica(tempo);
        Contest contest = contestBuilder.Result();
        contest.setStato(StatoContenuto.APPROVATO);
         //TODO
    }


    @Test
    public void TestPartecipaContest() {
        Tempo tempo = new Tempo();
        ContestBuilder contestBuilder = new ContestBuilder();
        contestBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        contestBuilder.BuildTitolo("Concorso fotografico");
        contestBuilder.BuildComune("ROMA");
        contestBuilder.BuildDescrizione("Concorso fotografico!!");
        tempo.setInizio(LocalDateTime.of(2024, 10, 20, 9, 0));
        tempo.setFine(LocalDateTime.of(2024, 10, 20, 12, 0));
        contestBuilder.BuildSpecifica(tempo);
        Contest contest = contestBuilder.Result();
        contest.setStato(StatoContenuto.APPROVATO);

        try{
            contestService.AggiungiContenuto(contest);
            contestService.PartecipaContest("Concorso fotografico", "ROMA", true, 17L);
        }catch (Exception e){
            fail("la partecipazione ha restituito un errore " + e.getMessage());
        }

        Contest contestAggiornato = contestRepository.findContestByTitoloAndComune("Concorso fotografico", "ROMA");

        assertTrue("il contest non è presente", contestAggiornato.getIdPartecipanti().contains(17L));

        assertEquals("il numero di partecipanti dovrebbe essere 1",1, contestAggiornato.getVotiFavore());
    }



    @Test
    public void TestHaiGiàPartecipato() {
        Tempo tempo = new Tempo();
        ContestBuilder contestBuilder = new ContestBuilder();
        contestBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        contestBuilder.BuildTitolo("Concorso fotografico");
        contestBuilder.BuildComune("ROMA");
        contestBuilder.BuildDescrizione("Concorso fotografico!!");
        tempo.setInizio(LocalDateTime.of(2024, 10, 20, 9, 0));
        tempo.setFine(LocalDateTime.of(2024, 10, 20, 12, 0));
        contestBuilder.BuildSpecifica(tempo);
        Contest contest = contestBuilder.Result();
        contest.setStato(StatoContenuto.APPROVATO);

        contest.getIdPartecipanti().add(15L);

        try{
            contestService.AggiungiContenuto(contest);
        }catch (Exception e){
            fail("l'aggiunta ha restituito un errore " + e.getMessage());
        }

        assertThrows(IllegalArgumentException.class, ()-> {
           contestService.PartecipaContest("Concorso fotografico", "ROMA", true, 15L);
       });

    }






}
