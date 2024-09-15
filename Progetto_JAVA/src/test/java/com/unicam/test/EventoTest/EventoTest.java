package com.unicam.test.EventoTest;

import com.unicam.Model.BuilderContenuto.EventoBuilder;
import com.unicam.Model.Evento;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.Tempo;
import com.unicam.Security.DataInitializer;
import com.unicam.Service.Contenuto.EventoService;
import com.unicam.Service.Contenuto.PuntoGeoService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import com.unicam.Service.UtenteService;
import com.unicam.dto.Risposte.EventoResponseDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EventoTest {


    @Autowired
    private EventoService eventoService;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private ProxyOSM proxyOSM;

    @Autowired
    private PuntoGeoService puntoGeoService;


    @Before
    public void setUp() throws Exception {
        dataInitializer.run();
    }

    @Test
    public void TestEventiPresenti() {
        assertTrue("non sono presenti eventi", !eventoService.GetEventiByComune("ROMA").isEmpty());
    }

    @Test
    public void TestAggiuntaEvento() {

        Tempo tempo = new Tempo();
        EventoBuilder eventoBuilder = new EventoBuilder();
        eventoBuilder.BuildComune("ROMA");
        eventoBuilder.BuildTitolo("sagra del vitello");
        eventoBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        eventoBuilder.BuildDescrizione("Bellissima sagra del vitello");
        tempo.setInizio(LocalDateTime.of(2024, 10, 20, 0, 0));  // 20 Ottobre 2024, 18:00
        tempo.setFine(LocalDateTime.of(2024, 10, 20, 0, 0));
        eventoBuilder.BuildSpecifica(tempo, puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI","ROMA"));
        Evento evento = eventoBuilder.Result();

        try{
            eventoService.AggiungiContenuto(evento);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }

        assertTrue("l'evento non è stato inserito correttamente", !eventoService.GetEventiByComune("ROMA").isEmpty());
    }


    @Test
    public void TestEliminaEvento() {
        eventoService.EliminaEvento("Festa della birra", "ROMA");

        assertFalse("L'evento 'Festa della birra' dovrebbe essere stato eliminato",
                eventoService.GetEventiByComune("ROMA")
                        .stream()
                        .anyMatch(evento -> "Festa della birra".equals(evento.getNomeEvento())));
    }

    @Test
    public void TestEliminaEventoNonEsistente() {
        assertThrows(NullPointerException.class, () -> eventoService.EliminaEvento("Evento non esistente", "ROMA"));
    }

    @Test
    public void TestRifiutoEvento() {
        Tempo tempo1 = new Tempo();
        EventoBuilder eventoBuilder = new EventoBuilder();
        eventoBuilder.BuildComune("ROMA");
        eventoBuilder.BuildTitolo("OpenBar");
        eventoBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        eventoBuilder.BuildDescrizione("OpenBar FREE ENTRY");
        tempo1.setInizio(LocalDateTime.of(2024, 10, 20, 0, 0));
        tempo1.setFine(LocalDateTime.of(2024, 10, 20, 0, 0));
        eventoBuilder.BuildSpecifica(tempo1, puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI","ROMA"));
        Evento evento = eventoBuilder.Result();
        evento.setStato(StatoContenuto.ATTESA);

        try{
            eventoService.AggiungiContenuto(evento);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }

        eventoService.AccettaORifiuta("OpenBar", "ROMA", StatoContenuto.RIFIUTATO);

        assertTrue("l'evento è ancora presente", eventoService.GetEventiByComune("ROMA").size() == 1);


    }

    @Test
    public void TestAccettazioneEvento() {
        Tempo tempo1 = new Tempo();
        EventoBuilder eventoBuilder = new EventoBuilder();
        eventoBuilder.BuildComune("ROMA");
        eventoBuilder.BuildTitolo("OpenBar");
        eventoBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        eventoBuilder.BuildDescrizione("OpenBar FREE ENTRY");
        tempo1.setInizio(LocalDateTime.of(2024, 10, 20, 0, 0));
        tempo1.setFine(LocalDateTime.of(2024, 10, 20, 0, 0));
        eventoBuilder.BuildSpecifica(tempo1, puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI","ROMA"));
        Evento evento = eventoBuilder.Result();
        evento.setStato(StatoContenuto.ATTESA);

        try{
            eventoService.AggiungiContenuto(evento);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }

        eventoService.AccettaORifiuta("OpenBar", "ROMA", StatoContenuto.APPROVATO);

        assertTrue("l'evento approvato non è presente ", eventoService.GetEventiByComune("ROMA").size() == 2);

    }


    @Test
    public void TestAggiuntaEventoConStessoNome() {

        Tempo tempo = new Tempo();
        EventoBuilder eventoBuilder = new EventoBuilder();
        eventoBuilder.BuildComune("ROMA");
        eventoBuilder.BuildTitolo("Festa della birra");
        eventoBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        eventoBuilder.BuildDescrizione("Bellissima festa della birra");
        tempo.setInizio(LocalDateTime.of(2024, 10, 20, 0, 0));  // 20 Ottobre 2024, 18:00
        tempo.setFine(LocalDateTime.of(2024, 11, 19, 0, 0));
        eventoBuilder.BuildSpecifica(tempo, puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI","ROMA"));
        Evento evento = eventoBuilder.Result();

        assertThrows(IllegalArgumentException.class, () -> eventoService.ControllaPresenzaNome(evento.getTitolo(), "ROMA"));

    }

    @Test
    public void TestAggiuntaEventiAccavallati() {
        Tempo tempo1 = new Tempo();
        EventoBuilder eventoBuilder = new EventoBuilder();
        eventoBuilder.BuildComune("ROMA");
        eventoBuilder.BuildTitolo("balli di gruppo");
        eventoBuilder.BuildAutore(utenteService.GetUtenteById(16L));
        eventoBuilder.BuildDescrizione("Bellissima gruppo");
        tempo1.setInizio(LocalDateTime.of(2024, 10, 20, 8, 0));
        tempo1.setFine(LocalDateTime.of(2024, 11, 19, 18, 0));
        eventoBuilder.BuildSpecifica(tempo1, puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI","ROMA"));
        Evento evento = eventoBuilder.Result();
        evento.setStato(StatoContenuto.ATTESA);
        eventoService.AggiungiContenuto(evento);

        Tempo tempo2 = new Tempo();
        EventoBuilder eventoBuilder2 = new EventoBuilder();
        eventoBuilder2.BuildComune("ROMA");
        eventoBuilder2.BuildTitolo("giochi da tavola");
        eventoBuilder2.BuildAutore(utenteService.GetUtenteById(16L));
        eventoBuilder2.BuildDescrizione("Bellissimi giochi");
        tempo2.setInizio(LocalDateTime.of(2024, 10, 20, 10, 0));
        tempo2.setFine(LocalDateTime.of(2024, 11, 19, 12, 0));
        eventoBuilder2.BuildSpecifica(tempo2, puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI","ROMA"));
        Evento evento2 = eventoBuilder2.Result();
        evento2.setStato(StatoContenuto.ATTESA);

        List<Evento> eventiInAttesa = new ArrayList<>();
        eventiInAttesa.add(evento2);

        eventoService.EliminaProposteEventiCheSiSovrappongono(eventiInAttesa, evento2);

        List<EventoResponseDTO> eventiEsistenti = eventoService.GetEventiByComune("ROMA");

        assertFalse("balli di gruppo presenti", eventiEsistenti.contains(evento));
        assertTrue("Giochi da tavola presenti", eventiInAttesa.contains(evento2));
    }

}





