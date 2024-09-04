package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.EliminaUtenteDTO;
import com.unicam.dto.Risposte.*;
import com.unicam.dto.RegistrazioneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/utente")
public class UtenteController {

    private UtenteService servizio;
    private ContenutoService<PuntoGeolocalizzato> servizioPuntoGeo;
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
    }

    @PostMapping("/RegistrazioneUtente")
    public ResponseEntity<LoginResponseDTO> Registrazione(@RequestBody RegistrazioneDTO registrazione){
        LoginResponseDTO login = new LoginResponseDTO();

        login.setMessage("Registrazione avvenuta con successo!");
        login.setToken(this.servizio.RegistrazioneUtente(registrazione));
        login.setRole(this.servizio.GetUtente(registrazione.getUsername()));
        login.setUsername(registrazione.getUsername());

        /*List<PuntoGeolocalizzato> puntiGeolocalizzati = this.servizioPuntoGeo.GetPuntiGeoByComune(registrazione.getComune());
        List<PuntoLogico> puntiLogici = this.servizioPuntoLo.GetPuntiLogiciByComune(registrazione.getComune());
        List<Itinerario> itinerari = this.servizioIti.GetItinerariByComune(registrazione.getComune());
        List<Evento> eventi = this.servizioEv.GetEventiByComune(registrazione.getComune());
        List<Contest> contest = this.servizioCon.GetContestByComuneRuolo(registrazione.getComune(), login.getRole());*/

        List<PuntoGeoResponseDTO> puntiGeolocalizzati = this.servizioPuntoGeo.GetPuntiGeoByComune(registrazione.getComune());
        List<PuntoLogicoResponseDTO> puntiLogici = this.servizioPuntoLo.GetPuntiLogiciByComune(registrazione.getComune());
        List<ItinerarioResponseDTO> itinerari = this.servizioIti.GetItinerariByComune(registrazione.getComune());
        List<EventoResponseDTO> eventi = this.servizioEv.GetEventiByComune(registrazione.getComune());
        List<ContestResponseDTO> contest = this.servizioCon.GetContestByComuneRuolo(registrazione.getComune(), login.getRole());


        login.getContenutiComune().put("punti geolocalizzati", puntiGeolocalizzati);
        login.getContenutiComune().put("punti logici / avvisi", puntiLogici);
        login.getContenutiComune().put("itinerari", itinerari);
        login.getContenutiComune().put("eventi", eventi);
        login.getContenutiComune().put("contest", contest);
        return ResponseEntity.ok(login);
    }

    @DeleteMapping("/EliminaAccount")
    public void EliminaAccount(@RequestBody EliminaUtenteDTO userDeleted){
        //TODO pensare come implementare l'eliminazione dell'account (tecnicamente un utente puo eliminare solo il suo account non quello di altri)
        this.servizio.EliminaUtenteByUsername(userDeleted.getUsername());
    }
}
