package com.unicam.Controller;

import com.unicam.Model.Ruolo;
import com.unicam.Security.JwtTokenProvider;
import com.unicam.Service.Contenuto.*;
import com.unicam.Service.UtenteService;
import com.unicam.dto.LoginDTO;
import com.unicam.dto.Risposte.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("api/auth")
public class AuthController {

    @Autowired
    private UtenteService servizioUtente;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private PuntoGeoService servizioPuntoGeo;
    @Autowired
    private PuntoLogicoService servizioPuntoLo;
    @Autowired
    private ItinerarioService servizioIti;
    @Autowired
    private EventoService servizioEv;
    @Autowired
    private ContestService servizioCon;

    @PostMapping("/Login")
    public ResponseEntity<LoginResponseDTO> Login(@RequestBody LoginDTO loginRequest) {
        LoginResponseDTO risposta = new LoginResponseDTO();

        risposta.setMessage("Login avvenuto con successo!");

        risposta.setToken(servizioUtente.LoginUtente(loginRequest.getUsername(), loginRequest.getPassword()));
        risposta.setUsername(loginRequest.getUsername());
        risposta.setRole(servizioUtente.GetUtente(loginRequest.getUsername()));

        String comune = this.servizioUtente.GetComuneByUsername(loginRequest.getUsername());
        risposta.setComune(comune);
        risposta.setComuneVisitato(comune);

        LocalDateTime adesso = LocalDateTime.now();

        if(!this.servizioUtente.GetUtente(loginRequest.getUsername()).equals(Ruolo.ADMIN)){
            List<PuntoGeoResponseDTO> puntiGeolocalizzati = this.servizioPuntoGeo.GetPuntiGeoByComune(comune);
            List<PuntoLogicoResponseDTO> puntiLogici = this.servizioPuntoLo.GetPuntiLogiciByComune(comune);
            List<ItinerarioResponseDTO> itinerari = this.servizioIti.GetItinerariByComune(comune);
            List<EventoResponseDTO> eventi = this.servizioEv.GetEventiByComune(comune);
            List<ContestResponseDTO> contest = this.servizioCon.GetContestByComuneRuolo(comune, adesso);

            risposta.getContenutiComune().put("punti geolocalizzati", puntiGeolocalizzati);
            risposta.getContenutiComune().put("punti logici / avvisi", puntiLogici);
            risposta.getContenutiComune().put("itinerari", itinerari);
            risposta.getContenutiComune().put("eventi", eventi);
            risposta.getContenutiComune().put("contest", contest);
        }
        return ResponseEntity.ok(risposta);
    }
}
