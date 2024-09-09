package com.unicam.Controller;

import com.unicam.Model.Ruolo;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Contenuto.*;
import com.unicam.Service.UtenteService;
import com.unicam.dto.Risposte.*;
import com.unicam.dto.RegistrazioneUtentiDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/utente")
public class UtenteController {

    private UtenteService servizio;
    /*private ContenutoService<PuntoGeolocalizzato> servizioPuntoGeo;
    private ContenutoService<PuntoLogico> servizioPuntoLo;
    private ContenutoService<Itinerario> servizioIti;
    private ContenutoService<Evento> servizioEv;
    private ContenutoService<Contest> servizioCon;

    @Autowired
    public UtenteController(UtenteService servizio,
                            ContenutoService<PuntoGeolocalizzato> servizioPuntoGeo,
                            ContenutoService<PuntoLogico> servizioPuntoLo,
                            ContenutoService<Itinerario> servizioIti,
                            ContenutoService<Evento> servizioEv,
                            ContenutoService<Contest> servizioCon){
        this.servizio = servizio;
        this.servizioPuntoGeo = servizioPuntoGeo;
        this.servizioPuntoLo = servizioPuntoLo;
        this.servizioIti = servizioIti;
        this.servizioEv = servizioEv;
        this.servizioCon = servizioCon;
    }*/

    private PuntoGeoService servizioPuntoGeo;
    private PuntoLogicoService servizioPuntoLo;
    private ItinerarioService servizioIti;
    private EventoService servizioEv;
    private ContestService servizioCon;

    @Autowired
    public UtenteController(UtenteService servizio,
                            PuntoGeoService servizioPuntoGeo,
                            PuntoLogicoService servizioPuntoLo,
                            ItinerarioService servizioIti,
                            EventoService servizioEv,
                            ContestService servizioCon){
        this.servizio = servizio;
        this.servizioPuntoGeo = servizioPuntoGeo;
        this.servizioPuntoLo = servizioPuntoLo;
        this.servizioIti = servizioIti;
        this.servizioEv = servizioEv;
        this.servizioCon = servizioCon;
    }

    @PostMapping("Api/RegistrazioneUtente")
    public ResponseEntity<LoginResponseDTO> Registrazione(@RequestBody RegistrazioneUtentiDTO registrazione){
        LoginResponseDTO login = new LoginResponseDTO();

        login.setMessage("Registrazione avvenuta con successo!");
        login.setToken(this.servizio.RegistrazioneUtente(registrazione));
        login.setRole(this.servizio.GetUtente(registrazione.getUsername()));
        login.setUsername(registrazione.getUsername());

        LocalDateTime adesso = LocalDateTime.now();

        List<PuntoGeoResponseDTO> puntiGeolocalizzati = this.servizioPuntoGeo.GetPuntiGeoByComune(registrazione.getComune());
        List<PuntoLogicoResponseDTO> puntiLogici = this.servizioPuntoLo.GetPuntiLogiciByComune(registrazione.getComune());
        List<ItinerarioResponseDTO> itinerari = this.servizioIti.GetItinerariByComune(registrazione.getComune());
        List<EventoResponseDTO> eventi = this.servizioEv.GetEventiByComune(registrazione.getComune());
        List<ContestResponseDTO> contest = this.servizioCon.GetContestByComuneRuolo(registrazione.getComune(), login.getRole(), adesso);


        login.getContenutiComune().put("punti geolocalizzati", puntiGeolocalizzati);
        login.getContenutiComune().put("punti logici / avvisi", puntiLogici);
        login.getContenutiComune().put("itinerari", itinerari);
        login.getContenutiComune().put("eventi", eventi);
        login.getContenutiComune().put("contest", contest);
        return ResponseEntity.ok(login);
    }

    @DeleteMapping("Api/Utente/Elimina-Account")
    public void EliminaAccount(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        if(currentRole.equals(Ruolo.ADMIN.name()) || currentRole.equals(Ruolo.COMUNE.name()))
            throw new IllegalArgumentException("Non puoi eliminare l'account");

        this.servizio.EliminaUtente(idUtente);
    }
}
