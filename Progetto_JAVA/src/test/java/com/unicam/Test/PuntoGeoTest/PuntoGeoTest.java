package com.unicam.Test.PuntoGeoTest;

import com.unicam.Model.BuilderContenuto.PuntoGeoBuilder;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.StatoContenuto;
import com.unicam.Security.DataInitializer;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.EventoService;
import com.unicam.Service.Contenuto.ItinerarioService;
import com.unicam.Service.Contenuto.PuntoGeoService;
import com.unicam.Service.Contenuto.PuntoLogicoService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import com.unicam.Service.UtenteService;
import com.unicam.dto.Risposte.ItinerarioResponseDTO;
import com.unicam.dto.Risposte.LuogoDTO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PuntoGeoTest {

    @Autowired
    private PuntoLogicoService puntoLogicoService;
    @Autowired
    private EventoService eventoService;
    @Autowired
    private ItinerarioService itinerarioService;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private ComuneService comuneService;
    @Autowired
    private DataInitializer dataInitializer;
    @Autowired
    private ProxyOSM proxyOSM;
    @Autowired
    private PuntoGeoService puntoGeoService;

    @Before
    public void setUp() throws Exception {
        dataInitializer.run();
    }

    @Test
    public void testPuntiApprovati(){
        assertTrue("I punti non sono stati inseriti", puntoGeoService.GetPuntiGeoByComune("ROMA").size() == 4);
    }

    @Test
    public void testPersistenza(){
        assertTrue("il comune non è inserito", comuneService.ContainComune("ROMA"));
    }

    @Test
    public void testComuneNumero(){
        assertTrue("il comune non è accettato", comuneService.GetAll().size() == 2);
    }

    @Test
    public void testComuneApprovato(){
        assertThrows(IllegalArgumentException.class, ()->comuneService.ControlloPresenzaComune("MILANO"));
    }

    @Test
    public void testInserimentoContenuto() throws IOException {
        try{
            comuneService.ControlloPresenzaComune("ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        PuntoGeoBuilder builder = new PuntoGeoBuilder();
        builder.BuildAutore(utenteService.GetUtenteById(14L));
        builder.BuildTitolo("Colosseo");
        builder.BuildDescrizione("Anfiteatro Flavio");
        builder.BuildComune("ROMA");
        List<Double> coordinate = proxyOSM.getCoordinates("Colosseo,Roma");
        builder.BuildSpecifica(coordinate.get(0), coordinate.get(1));
        PuntoGeolocalizzato punto = builder.Result();
        punto.setStato(StatoContenuto.APPROVATO);
        try{
            puntoGeoService.AggiungiContenuto(punto);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Il punto non è stato inserito correttamente", puntoGeoService.GetPuntiGeoByComune("ROMA").size() == 5);
    }

    @Test
    public void testInserimentoContenutoStessoNome() throws IOException {
        try{
            comuneService.ControlloPresenzaComune("ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        PuntoGeoBuilder builder = new PuntoGeoBuilder();
        builder.BuildAutore(utenteService.GetUtenteById(14L));
        builder.BuildTitolo("Colosseo");
        builder.BuildDescrizione("Anfiteatro Flavio");
        builder.BuildComune("ROMA");
        List<Double> coordinate = proxyOSM.getCoordinates("Colosseo,Roma");
        builder.BuildSpecifica(coordinate.get(0), coordinate.get(1));
        PuntoGeolocalizzato punto = builder.Result();
        punto.setStato(StatoContenuto.APPROVATO);
        try{
            puntoGeoService.AggiungiContenuto(punto);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }

        assertTrue("Il punto non è stato inserito correttamente", puntoGeoService.GetPuntiGeoByComune("ROMA").size() == 5);

        PuntoGeoBuilder builder2 = new PuntoGeoBuilder();
        builder2.BuildAutore(utenteService.GetUtenteById(14L));
        builder2.BuildTitolo("Colosseo");
        builder2.BuildDescrizione("Anfiteatro Flavio");
        builder2.BuildComune("ROMA");
        List<Double> coordinate2 = proxyOSM.getCoordinates("Colosseo,Roma");
        builder.BuildSpecifica(coordinate.get(0), coordinate.get(1));
        PuntoGeolocalizzato punto2 = builder.Result();
        assertThrows(IllegalArgumentException.class, ()-> puntoGeoService.ControllaPresenzaNome(punto2.getTitolo(), punto2.getComune()));

        assertTrue("Il punto non è stato inserito correttamente", puntoGeoService.GetPuntiGeoByComune("ROMA").size() == 5);
    }

    @Test
    public void testInserimentoContenutoRipetutoFallimentare() throws IOException {
        try{
            comuneService.ControlloPresenzaComune("ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        PuntoGeoBuilder builder = new PuntoGeoBuilder();
        builder.BuildAutore(utenteService.GetUtenteById(14L));
        builder.BuildTitolo("Colosseo");
        builder.BuildDescrizione("Anfiteatro Flavio");
        builder.BuildComune("ROMA");
        List<Double> coordinate = proxyOSM.getCoordinates("Colosseo,Roma");
        builder.BuildSpecifica(coordinate.get(0), coordinate.get(1));
        PuntoGeolocalizzato punto = builder.Result();
        punto.setStato(StatoContenuto.APPROVATO);
        try{
            puntoGeoService.AggiungiContenuto(punto);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }

        assertTrue("Il punto non è stato inserito correttamente", puntoGeoService.GetPuntiGeoByComune("ROMA").size() == 5);

        PuntoGeoBuilder builder2 = new PuntoGeoBuilder();
        builder2.BuildAutore(utenteService.GetUtenteById(14L));
        builder2.BuildTitolo("ARCO DI TITO");
        builder2.BuildDescrizione("Anfiteatro Flavio");
        builder2.BuildComune("ROMA");
        List<Double> coordinate2 = proxyOSM.getCoordinates("Colosseo,Roma");
        builder.BuildSpecifica(coordinate2.get(0), coordinate2.get(1));
        PuntoGeolocalizzato punto2 = builder.Result();
        punto2.setStato(StatoContenuto.APPROVATO);
        assertThrows(IllegalArgumentException.class, ()-> puntoGeoService.AggiungiContenuto(punto2));

        assertTrue("Il punto non è stato inserito correttamente", puntoGeoService.GetPuntiGeoByComune("ROMA").size() == 5);
    }

    @Test
    public void testAggiuntaPreferito(){
        try{
            puntoGeoService.AggiuntiPreferito("ARCO DI TITO", "ROMA", 13L);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Punto non aggiunto ai preferiti", puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("ARCO DI TITO", "ROMA").getIdUtenteContenutoPreferito().contains(13L));
    }

    @Test
    public void testAggiuntaPreferitoRipetutaFallita(){
        try{
            puntoGeoService.AggiuntiPreferito("FONTANA DI TREVI", "ROMA", 13L);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Punto non aggiunto ai preferiti", puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI", "ROMA").getIdUtenteContenutoPreferito().contains(13L));
        assertThrows(IllegalArgumentException.class, () -> puntoGeoService.AggiuntiPreferito("FONTANA DI TREVI", "ROMA", 13L));
    }

    @Test
    public void testSegnalazione(){
        try{
            puntoGeoService.SegnalaContenuto("ARCO DI TITO", "ROMA");
        }catch(Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Segnalazione non avvenuta", puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("ARCO DI TITO", "ROMA").getStato() == StatoContenuto.SEGNALATO);

    }

    @Test
    public void testSegnalazioneRipetutaFallita(){
        try{
            puntoGeoService.SegnalaContenuto("PANTHEON", "ROMA");
        }catch(Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Segnalazione non avvenuta", puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("PANTHEON", "ROMA").getStato() == StatoContenuto.SEGNALATO);
        assertThrows(IllegalArgumentException.class, () -> puntoGeoService.SegnalaContenuto("PANTHEON", "ROMA"));
    }

    @Test
    public void testAccettaAttesa(){
        try{
            puntoGeoService.AccettaORifiuta("PIAZZA NAVONA", "ROMA", StatoContenuto.APPROVATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Il punto non è stato accettato", puntoGeoService.GetPuntiGeoByComune("ROMA").size()==5);
    }

    @Test
    public void testAccettaAttesaRipetuto(){
        try{
            puntoGeoService.AccettaORifiuta("PIAZZA NAVONA", "ROMA", StatoContenuto.APPROVATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Il punto non è stato accettato", puntoGeoService.GetPuntiGeoByComune("ROMA").size() == 5);
        assertThrows(IllegalArgumentException.class, () -> puntoGeoService.AccettaORifiuta("PIAZZA NAVONA", "ROMA", StatoContenuto.APPROVATO));
    }

    @Test
    public void testRifiutaAttesa(){
        try{
            puntoGeoService.AccettaORifiuta("PIAZZA NAVONA", "ROMA", StatoContenuto.RIFIUTATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Il punto non è stato accettato", puntoGeoService.GetPuntiGeoByComune("ROMA").size()==4);
    }

    @Test
    public void testAccettaSegnalazione(){
        try{
            puntoGeoService.SegnalaContenuto("ARCO DI TITO", "ROMA");
        }catch(Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Segnalazione non avvenuta", puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("ARCO DI TITO", "ROMA").getStato() == StatoContenuto.SEGNALATO);
        assertTrue("Il punto non è stato accettato", puntoGeoService.GetPuntiGeoByComune("ROMA").size()==3);
        try{
            puntoGeoService.AccettaORifiuta("PIAZZA NAVONA", "ROMA", StatoContenuto.APPROVATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Il punto non è stato accettato", puntoGeoService.GetPuntiGeoByComune("ROMA").size()==4);
    }

    @Test
    public void testRifiutaSegnalazione(){
        try{
            puntoGeoService.SegnalaContenuto("ARCO DI TITO", "ROMA");
        }catch(Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Segnalazione non avvenuta", puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("ARCO DI TITO", "ROMA").getStato() == StatoContenuto.SEGNALATO);
        assertTrue("Il punto non è stato accettato", puntoGeoService.GetPuntiGeoByComune("ROMA").size()==3);
        try{
            puntoGeoService.AccettaORifiuta("PIAZZA NAVONA", "ROMA", StatoContenuto.RIFIUTATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Il punto non è stato accettato", puntoGeoService.GetPuntiGeoByComune("ROMA").size()==3);
    }

    @Test
    public void testEliminaPunto(){
        PuntoGeolocalizzato punto = puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI", "ROMA");
        LuogoDTO luogo = new LuogoDTO(punto.getTitolo(), punto.getLatitudine(), punto.getLongitudine());
        try{
            puntoGeoService.EliminaPuntoGeo("FONTANA DI TREVI", "ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        List<ItinerarioResponseDTO> itinerari = itinerarioService.GetItinerariByComune("ROMA");
        for(ItinerarioResponseDTO itinerario: itinerari){
            List<LuogoDTO> punti = itinerario.getLuogo();
            assertFalse("Il punto non è stato eliminato", punti.contains(luogo));
        }
        assertTrue("Il punto non è stato eliminato", eventoService.GetEventiByComune("ROMA").size() == 0);
        assertFalse("Il punto non è stato eliminato", puntoLogicoService.ContienePuntoLogico("AVVISO!! AFFOLLATO", "ROMA"));
    }
}
