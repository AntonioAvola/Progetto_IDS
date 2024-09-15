package com.unicam.test.ItinerarioTest;

import com.unicam.Model.Itinerario;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.StatoContenuto;
import com.unicam.Repository.Contenuto.ItinerarioRepository;
import com.unicam.Security.DataInitializer;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.ItinerarioService;
import com.unicam.Service.Contenuto.PuntoGeoService;
import com.unicam.Service.UtenteService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItinerarioTest {

    @Autowired
    private ComuneService comuneService;
    @Autowired
    private ItinerarioService itinerarioService;
    @Autowired
    private ItinerarioRepository itinerarioRepository;
    @Autowired
    private PuntoGeoService puntoGeoService;
    @Autowired
    private UtenteService utenteService;

    @Autowired
    private DataInitializer dataInitializer;

    @Before
    public void setUp() throws Exception {
        dataInitializer.run();
    }


    @Test
    public void testAggiuntaItinerario(){
        try{
            comuneService.ControlloPresenzaComune("ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        Itinerario itinerio1 = new Itinerario();
        itinerio1.setAutore(utenteService.GetUtenteById(15L));
        itinerio1.setTitolo("CORSA");
        itinerio1.setDescrizione("Per chi vuole un po' di cambiamento nell'inclinazione del terreno");
        itinerio1.setComune("ROMA");
        List<PuntoGeolocalizzato> interessi1 = new ArrayList<>();
        interessi1.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI", "ROMA"));
        interessi1.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("ARCO DI TITO", "ROMA"));
        interessi1.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("PANTHEON", "ROMA"));
        itinerio1.setPuntiDiInteresse(interessi1);
        itinerio1.setStato(StatoContenuto.APPROVATO);
        try{
            itinerarioService.AggiungiContenuto(itinerio1);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
    }

    @Test
    public void testAggiuntaItinerarioStessoNomeFallito(){
        try{
            comuneService.ControlloPresenzaComune("ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        Itinerario itinerio1 = new Itinerario();
        itinerio1.setAutore(utenteService.GetUtenteById(15L));
        itinerio1.setTitolo("CORSA");
        itinerio1.setDescrizione("Per chi vuole un po' di cambiamento nell'inclinazione del terreno");
        itinerio1.setComune("ROMA");
        List<PuntoGeolocalizzato> interessi1 = new ArrayList<>();
        interessi1.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI", "ROMA"));
        interessi1.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("ARCO DI TITO", "ROMA"));
        interessi1.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("PANTHEON", "ROMA"));
        itinerio1.setPuntiDiInteresse(interessi1);
        itinerio1.setStato(StatoContenuto.APPROVATO);
        try{
            itinerarioService.AggiungiContenuto(itinerio1);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("L'itinerario non è stato aggiunto correttamente", itinerarioService.GetItinerariByComune("ROMA").size() == 3);

        Itinerario itinerio2 = new Itinerario();
        itinerio2.setAutore(utenteService.GetUtenteById(15L));
        itinerio2.setTitolo("CORSA");
        itinerio2.setDescrizione("Per chi vuole un po' di cambiamento nell'inclinazione del terreno");
        itinerio2.setComune("ROMA");
        List<PuntoGeolocalizzato> interessi2 = new ArrayList<>();
        interessi2.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI", "ROMA"));
        interessi2.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("ARCO DI TITO", "ROMA"));
        interessi2.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("PANTHEON", "ROMA"));
        itinerio2.setPuntiDiInteresse(interessi1);
        itinerio2.setStato(StatoContenuto.APPROVATO);
        assertThrows(IllegalArgumentException.class, () -> itinerarioService.ControllaPresenzaNome("CORSA", "ROMA"));
    }

    @Test
    public void testAggiuntiItinerioNomeDiversoStessiPuntiFallito(){
        try{
            comuneService.ControlloPresenzaComune("ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        Itinerario itinerio1 = new Itinerario();
        itinerio1.setAutore(utenteService.GetUtenteById(15L));
        itinerio1.setTitolo("CORSA");
        itinerio1.setDescrizione("Per chi vuole un po' di cambiamento nell'inclinazione del terreno");
        itinerio1.setComune("ROMA");
        List<PuntoGeolocalizzato> interessi1 = new ArrayList<>();
        interessi1.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI", "ROMA"));
        interessi1.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("ARCO DI TITO", "ROMA"));
        interessi1.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("PANTHEON", "ROMA"));
        itinerio1.setPuntiDiInteresse(interessi1);
        itinerio1.setStato(StatoContenuto.APPROVATO);
        try{
            itinerarioService.AggiungiContenuto(itinerio1);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("L'itinerario non è stato aggiunto correttamente", itinerarioService.GetItinerariByComune("ROMA").size() == 3);

        Itinerario itinerio2 = new Itinerario();
        itinerio2.setAutore(utenteService.GetUtenteById(15L));
        itinerio2.setTitolo("JOGGING");
        itinerio2.setDescrizione("Per tenersi in allenamento");
        itinerio2.setComune("ROMA");
        List<PuntoGeolocalizzato> interessi2 = new ArrayList<>();
        interessi2.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("FONTANA DI TREVI", "ROMA"));
        interessi2.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("ARCO DI TITO", "ROMA"));
        interessi2.add(puntoGeoService.GetPuntoGeoByNomeAndComuneAndStato("PANTHEON", "ROMA"));
        itinerio2.setPuntiDiInteresse(interessi1);
        itinerio2.setStato(StatoContenuto.APPROVATO);
        assertThrows(IllegalArgumentException.class, () -> itinerarioService.ControllaPresenzaNome("CORSA", "ROMA"));
    }

    @Test
    public void testAccettaItinerarioAttesa(){
        try{
            itinerarioService.AccettaORifiuta("CAMMINATA CENTRO STORICO", "ROMA", StatoContenuto.APPROVATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Fallita accettazione itinerario", itinerarioService.GetItinerariByComune("ROMA").size() == 3);
    }

    @Test
    public void testAccettaItinerarioRipetutoFallito(){
        try{
            itinerarioService.AccettaORifiuta("CAMMINATA CENTRO STORICO", "ROMA", StatoContenuto.APPROVATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Fallita accettazione itinerario", itinerarioService.GetItinerariByComune("ROMA").size() == 3);
        assertThrows(IllegalArgumentException.class, () -> itinerarioService.AccettaORifiuta("CAMMINATA CENTRO STORICO", "ROMA", StatoContenuto.APPROVATO));
    }

    @Test
    public void testRifiutaItinerarioAttesa(){
        try{
            itinerarioService.AccettaORifiuta("CAMMINATA CENTRO STORICO", "ROMA", StatoContenuto.RIFIUTATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Fallito rifiuto itinerario", itinerarioService.GetItinerariByComune("ROMA").size() == 2);
        try{
            itinerarioService.ControllaPresenzaNome("CAMMINATA CENTRO STORICO", "ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
    }

    @Test
    public void testRifiutaItinerarioAttesaRipetutoFallito(){
        try{
            itinerarioService.AccettaORifiuta("CAMMINATA CENTRO STORICO", "ROMA", StatoContenuto.RIFIUTATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Fallito rifiuto itinerario", itinerarioService.GetItinerariByComune("ROMA").size() == 2);
        try{
            itinerarioService.ControllaPresenzaNome("CAMMINATA CENTRO STORICO", "ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertThrows(IllegalArgumentException.class, () -> itinerarioService.AccettaORifiuta("CAMMINATA CENTRO STORICO", "ROMA", StatoContenuto.RIFIUTATO));
    }

    @Test
    public void testAggiuntiAPreferiti(){
        try{
            itinerarioService.AggiungiPreferito("SCOUT", "ROMA", 12L);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        Itinerario itinerario = this.itinerarioRepository.findItinerarioByTitoloAndComune("SCOUT", "ROMA");
        assertTrue("Aggiunta ai preferiti non riuscita", itinerario.getIdUtenteContenutoPreferito().contains(12L));
    }

    @Test
    public void testAggiuntiAPreferitiRipetutoFallito(){
        try{
            itinerarioService.AggiungiPreferito("SCOUT", "ROMA", 12L);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        try{
            itinerarioService.AggiungiPreferito("SCOUT", "ROMA", 11L);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        Itinerario itinerario = this.itinerarioRepository.findItinerarioByTitoloAndComune("SCOUT", "ROMA");
        assertTrue("Aggiunta ai preferiti non riuscita", itinerario.getIdUtenteContenutoPreferito().contains(12L));
        assertTrue("Aggiunta ai preferiti non riuscita", itinerario.getIdUtenteContenutoPreferito().contains(11L));
        assertThrows(IllegalArgumentException.class, () -> itinerarioService.AggiungiPreferito("SCOUT", "ROMA", 12L));
    }

    @Test
    public void testSegnalaItinerario(){
        try{
            itinerarioService.SegnalaContenuto("CAMMINATA", "ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Segnalazione non avvenuta", this.itinerarioRepository.findItinerarioByTitoloAndComune("CAMMINATA", "ROMA").getStato() == StatoContenuto.SEGNALATO);
    }

    @Test
    public void testSegnalaItinerarioRipetutoFallito(){
        try{
            itinerarioService.SegnalaContenuto("CAMMINATA", "ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertThrows(IllegalArgumentException.class, () -> itinerarioService.SegnalaContenuto("CAMMINATA", "ROMA"));
    }

    @Test
    public void testAccettaItinerarioSegnalato(){
        try{
            itinerarioService.SegnalaContenuto("CAMMINATA", "ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        Itinerario itinerio = this.itinerarioRepository.findItinerarioByTitoloAndComune("CAMMINATA", "ROMA");
        assertTrue("Segnalazione non avvenuta", itinerio.getStato() == StatoContenuto.SEGNALATO);

        try{
            itinerarioService.AccettaORifiuta("CAMMINATA", "ROMA", StatoContenuto.APPROVATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Segnalazione non accettata", itinerarioService.GetItinerariByComune("ROMA").size() == 2);
    }

    @Test
    public void testRifiutaItinerarioSegnalato(){
        try{
            itinerarioService.SegnalaContenuto("SCOUT", "ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        Itinerario itinerio = this.itinerarioRepository.findItinerarioByTitoloAndComune("SCOUT", "ROMA");
        assertTrue("Segnalazione non avvenuta", itinerio.getStato() == StatoContenuto.SEGNALATO);

        try{
            itinerarioService.AccettaORifiuta("SCOUT", "ROMA", StatoContenuto.RIFIUTATO);
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Segnalazione non accettata", itinerarioService.GetItinerariByComune("ROMA").size() == 1);
    }

    @Test
    public void testElimina(){
        try{
            itinerarioService.EliminaItinerario("SCOUT", "ROMA");
        }catch (Exception e){
            fail("Il metodo ha lanciato un errore: " + e.getMessage());
        }
        assertTrue("Eliminazione itinerario non riuscita", itinerarioService.GetItinerariByComune("ROMA").size() == 1);
    }
}
