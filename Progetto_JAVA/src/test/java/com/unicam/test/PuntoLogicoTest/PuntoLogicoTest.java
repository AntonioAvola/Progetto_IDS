package com.unicam.test.PuntoLogicoTest;

import com.unicam.Model.PuntoLogico;
import com.unicam.Model.StatoContenuto;
import com.unicam.Security.DataInitializer;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.PuntoGeoService;
import com.unicam.Service.Contenuto.PuntoLogicoService;
import com.unicam.Service.UtenteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class PuntoLogicoTest {

    @Autowired
    private PuntoGeoService puntoGeoService;
    @Autowired
    private PuntoLogicoService logicoService;
    @Autowired
    private UtenteService utenteService;
    @Autowired
    private ComuneService comuneService;
    @Autowired
    private DataInitializer dataInitializer;

    @Before
    public void setUp() throws Exception {
        dataInitializer.run();
    }

    @Test
    public void testAggiuntaAvviso(){
        assertTrue("Gli avvisi non sono stati caricati", logicoService.ContienePuntoLogico("AVVISO!! RISTRUTTURAZIONE", "ROMA"));
        assertTrue("L'avviso non è stato inserito con successo", logicoService.GetPuntiLogiciByComune("ROMA").size() == 2);
        try{
            comuneService.ControlloPresenzaComune("ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        PuntoLogico punto1 = new PuntoLogico();
        punto1.setAutore(utenteService.GetUtenteById(15L));
        punto1.setTitolo("AVVISO!! RISTRUTTURAZIONE");
        punto1.setDescrizione("Monumento sottoposto a ristrutturazione. Si potrà tornare a fisitarlo dal 15 settembre");
        punto1.setComune("ROMA");
        punto1.setRiferimento(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI", "ROMA"));
        punto1.setStato(StatoContenuto.APPROVATO);
        try{
            logicoService.AggiungiContenuto(punto1);
        }catch (Exception e){
            fail("Il metodo ha lanciato un'eccezione: " + e.getMessage());
        }
        assertTrue("L'avviso non è stato inserito con successo", logicoService.GetPuntiLogiciByComune("ROMA").size() == 3);
    }

    @Test
    public void testAggiuntaAvvisoPresenteFallito(){
        assertTrue("Gli avvisi non sono stati caricati", logicoService.ContienePuntoLogico("AVVISO!! RISTRUTTURAZIONE", "ROMA"));
        assertTrue("L'avviso non è stato inserito con successo", logicoService.GetPuntiLogiciByComune("ROMA").size() == 2);
        try{
            comuneService.ControlloPresenzaComune("ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        PuntoLogico punto1 = new PuntoLogico();
        punto1.setAutore(utenteService.GetUtenteById(15L));
        punto1.setTitolo("AVVISO!! RISTRUTTURAZIONE");
        punto1.setDescrizione("Monumento sottoposto a ristrutturazione. Si potrà tornare a visitarlo dal 15 settembre");
        punto1.setComune("ROMA");
        punto1.setRiferimento(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI", "ROMA"));
        punto1.setStato(StatoContenuto.APPROVATO);
        try{
            logicoService.AggiungiContenuto(punto1);
        }catch (Exception e){
            fail("Il metodo ha lanciato un'eccezione: " + e.getMessage());
        }
        assertTrue("L'avviso non è stato inserito con successo", logicoService.GetPuntiLogiciByComune("ROMA").size() == 3);

        PuntoLogico punto2 = new PuntoLogico();
        punto2.setAutore(utenteService.GetUtenteById(17L));
        punto2.setTitolo("AVVISO!! RISTRUTTURAZIONE");
        punto2.setDescrizione("Monumento sottoposto a ristrutturazione. Si potrà tornare a visitarla dal 20 settembre");
        punto2.setComune("ROMA");
        punto2.setRiferimento(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI", "ROMA"));
        punto2.setStato(StatoContenuto.ATTESA);
        assertThrows(IllegalArgumentException.class, () -> logicoService.AggiungiContenuto(punto2));
    }

    @Test
    public void testAggiuntaAvvisoStessoNomePuntoDifferente(){
        assertTrue("Gli avvisi non sono stati caricati", logicoService.ContienePuntoLogico("AVVISO!! RISTRUTTURAZIONE", "ROMA"));
        assertTrue("L'avviso non è stato inserito con successo", logicoService.GetPuntiLogiciByComune("ROMA").size() == 2);
        try{
            comuneService.ControlloPresenzaComune("ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        PuntoLogico punto1 = new PuntoLogico();
        punto1.setAutore(utenteService.GetUtenteById(15L));
        punto1.setTitolo("AVVISO!! RISTRUTTURAZIONE");
        punto1.setDescrizione("Monumento sottoposto a ristrutturazione. Si potrà tornare a visitarla dal 20 settembre");
        punto1.setComune("ROMA");
        punto1.setRiferimento(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI", "ROMA"));
        punto1.setStato(StatoContenuto.APPROVATO);
        try{
            logicoService.AggiungiContenuto(punto1);
        }catch (Exception e){
            fail("Il metodo ha lanciato un'eccezione: " + e.getMessage());
        }

        PuntoLogico punto2 = new PuntoLogico();
        punto2.setAutore(utenteService.GetUtenteById(17L));
        punto2.setTitolo("AVVISO!! RISTRUTTURAZIONE");
        punto2.setDescrizione("Monumento sottoposto a ristrutturazione. Si potrà tornare a vosotarla dal 30 settembre");
        punto2.setComune("ROMA");
        punto2.setRiferimento(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("ARCO DI TITO", "ROMA"));
        punto2.setStato(StatoContenuto.APPROVATO);
        try{
            logicoService.AggiungiContenuto(punto2);
        }catch (Exception e){
            fail("Il metodo ha lanciato un'eccezione: " + e.getMessage());
        }

        assertTrue("L'avviso non è stato inserito con successo", logicoService.GetPuntiLogiciByComune("ROMA").size() == 4);
    }

    @Test
    public void testAccettaAvviso(){
        try{
            logicoService.AccettaORifiuta("AVVISO!! AFFOLLATO", "FONTANA DI TREVI", "ROMA", StatoContenuto.APPROVATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Avviso non accettato", logicoService.GetPuntiLogiciByComune("ROMA").size() == 3);
    }

    @Test
    public void testAccettaAvvisoRipetuto(){
        try{
            logicoService.AccettaORifiuta("AVVISO!! AFFOLLATO", "FONTANA DI TREVI", "ROMA", StatoContenuto.APPROVATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Avviso non accettato", logicoService.GetPuntiLogiciByComune("ROMA").size() == 3);

        assertThrows(IllegalArgumentException.class, () -> logicoService.AccettaORifiuta("AVVISO!! AFFOLLATO", "FONTANA DI TREVI", "ROMA", StatoContenuto.APPROVATO));
        assertThrows(IllegalArgumentException.class, () -> logicoService.AccettaORifiuta("AVVISO!! LAVORI IN CORSO", "ARCO DI TITO", "ROMA", StatoContenuto.APPROVATO));
        assertThrows(IllegalArgumentException.class, () -> logicoService.AccettaORifiuta("AFFOLLATO", "PANTHEON", "ROMA", StatoContenuto.APPROVATO));
    }

    @Test
    public void testRifiuta(){
        try{
            logicoService.AccettaORifiuta("AVVISO!! AFFOLLATO", "FONTANA DI TREVI", "ROMA", StatoContenuto.RIFIUTATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Avviso non rifiutato", logicoService.GetPuntiLogiciByComune("ROMA").size() == 2);
        assertFalse("Avviso non rifiutato", logicoService.ContienePuntoLogico("AVVISO!! AFFOLLATO", "ROMA"));
    }

    @Test
    public void testRifiutaRipetuto(){
        try{
            logicoService.AccettaORifiuta("AVVISO!! AFFOLLATO", "FONTANA DI TREVI", "ROMA", StatoContenuto.RIFIUTATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Avviso non rifiutato", logicoService.GetPuntiLogiciByComune("ROMA").size() == 2);
        assertFalse("Avviso non rifiutato", logicoService.ContienePuntoLogico("AVVISO!! AFFOLLATO", "ROMA"));

        assertThrows(IllegalArgumentException.class, () -> logicoService.AccettaORifiuta("AVVISO!! AFFOLLATO", "FONTANA DI TREVI", "ROMA", StatoContenuto.RIFIUTATO));
        assertThrows(IllegalArgumentException.class, () -> logicoService.AccettaORifiuta("AFFOLLATO", "FONTANA DI TREVI", "ROMA", StatoContenuto.RIFIUTATO));
        assertThrows(IllegalArgumentException.class, () -> logicoService.AccettaORifiuta("AVVISO!! AFFOLLATO", "PANTHEON", "ROMA", StatoContenuto.RIFIUTATO));

    }
    @Test
    public void testElimina(){
        try{
            logicoService.EliminaPuntoLogico("AVVISO!! LAVORI IN CORSO", "ROMA", "ARCO DI TITO", 15);
        }catch(Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("L'avviso non è stato eliminato", logicoService.GetPuntiLogiciByComune("ROMA").size() == 1);
    }
}
