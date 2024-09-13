package com.unicam.Test.PuntoGeoTest;

import com.unicam.Model.BuilderContenuto.PuntoGeoBuilder;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.StatoContenuto;
import com.unicam.Security.DataInitializer;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.PuntoGeoService;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import com.unicam.Service.UtenteService;
import org.checkerframework.checker.units.qual.A;
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
public class InserimentoContenuti {

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

}
