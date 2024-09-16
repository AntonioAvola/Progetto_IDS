package com.unicam.test.UserTest;


import com.unicam.Model.Ruolo;
import com.unicam.Model.User;
import com.unicam.Repository.Contenuto.ContestRepository;
import com.unicam.Repository.Contenuto.EventoRepository;
import com.unicam.Repository.Contenuto.ItinerarioRepository;
import com.unicam.Repository.Contenuto.PuntoGeoRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.Security.DataInitializer;
import com.unicam.Service.Contenuto.*;
import com.unicam.Service.UtenteService;
import com.unicam.dto.RegistrazioneUtentiDTO;
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
public class UserTest {

    @Autowired
    private UtenteService utenteService;

    @Autowired
    private DataInitializer dataInitializer;

   @Autowired
   private ItinerarioService itinerarioService;

    @Autowired
    private EventoService eventoService;

    @Autowired
    private PuntoGeoService puntoGeoService;

    @Autowired
    private UtenteRepository repoUtente;

    @Autowired
    private ItinerarioRepository itinerarioRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private PuntoLogicoService puntoLogicoService;

    @Autowired
    private ContestRepository contestRepository;



    @Before
    public void setUp() throws Exception{
        dataInitializer.run();
    }


    @Test
    public void TestRegistrazioneSuccesso() {
        RegistrazioneUtentiDTO registrazioneUtentiDTO = new RegistrazioneUtentiDTO();
        registrazioneUtentiDTO.setName("Antonio");
        registrazioneUtentiDTO.setComune("ROMA");
        registrazioneUtentiDTO.setUsername("anto200");
        registrazioneUtentiDTO.setEmail("antonio@gmail.com");
        registrazioneUtentiDTO.setPassword("Root111!");
        registrazioneUtentiDTO.setAnimatore(false);
        registrazioneUtentiDTO.setCuratore(false);
        registrazioneUtentiDTO.setRappresentanteComune(false);

        try{
            utenteService.RegistrazioneUtente(registrazioneUtentiDTO);
        }catch (Exception e){
            fail("la registrazione dell'utente ha restituito un problema" + e.getMessage());
        }

    }

    @Test
    public void TestRegistrazioneFallimentare(){
        RegistrazioneUtentiDTO registrazioneUtentiDTO = new RegistrazioneUtentiDTO();
        registrazioneUtentiDTO.setName("Antonio");
        registrazioneUtentiDTO.setComune("ROMA");
        registrazioneUtentiDTO.setUsername("anto200");
        registrazioneUtentiDTO.setEmail("antonio@gmail.com");
        registrazioneUtentiDTO.setPassword("password");
        registrazioneUtentiDTO.setAnimatore(false);
        registrazioneUtentiDTO.setCuratore(false);
        registrazioneUtentiDTO.setRappresentanteComune(false);

        assertThrows(IllegalArgumentException.class,() -> utenteService.RegistrazioneUtente(registrazioneUtentiDTO));
    }


    public void TestLogin() {
        try{
            utenteService.LoginUtente("animatore4", "Animatore111!");
        }catch (Exception e){
            fail("il login ha restituito un errore " + e.getMessage());
        }
    }

    public void LoginFallimentare(){
        assertThrows(IllegalArgumentException.class,()->  utenteService.LoginUtente("utente", "inesistente"));
    }

    @Test
    public void TestEliminaAccount(){
        RegistrazioneUtentiDTO registrazioneUtentiDTO = new RegistrazioneUtentiDTO();
        registrazioneUtentiDTO.setName("Antonio");
        registrazioneUtentiDTO.setComune("ROMA");
        registrazioneUtentiDTO.setUsername("anto200");
        registrazioneUtentiDTO.setEmail("antonio@gmail.com");
        registrazioneUtentiDTO.setPassword("Root111!");
        registrazioneUtentiDTO.setAnimatore(false);
        registrazioneUtentiDTO.setCuratore(false);
        registrazioneUtentiDTO.setRappresentanteComune(false);

        utenteService.RegistrazioneUtente(registrazioneUtentiDTO);
        User utenteEliminato = repoUtente.findByUsername("anto200");


        try{
            utenteService.EliminaUtente(utenteEliminato.getId());
            utenteService.EliminaUtente(16);
            utenteService.EliminaUtente(15);
        }catch (Exception e){
            fail("l'eliminazione ha restituito un errore " + e.getMessage());
        }

        assertTrue("i contenuti esistono ancora", eventoRepository.findAll().isEmpty());
        assertTrue( "i contenuti esistono ancora", contestRepository.findAll().isEmpty());
        assertEquals("i contenuti esistono ancora", 0, itinerarioService.GetItinerariByComune("ROMA").size());
        assertEquals("i contenuti esistono ancora", 1, puntoGeoService.GetPuntiGeoByComune("ROMA").size());
        assertEquals("i contenuti esistono ancora", 0, puntoLogicoService.GetPuntiLogiciByComune("ROMA").size());


    }

    @Test
    public void TestVisitaComune() {
        try{
            utenteService.AggiornaComuneVisitato(11L, "ROMA");
        }catch (Exception e){
            fail("l'aggiornamento del comune visitato ha restituito un errore  " + e.getMessage());
        }

        User utente = utenteService.GetUtenteById(11L);
        assertEquals("il comune visitato non risultà essere quello aspettato", "ROMA", utente.getComuneVisitato());
    }
}
