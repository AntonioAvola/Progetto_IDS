package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaComune;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.*;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.RicercaComuneDTO;
import com.unicam.dto.RichiestaComuneDTO;
import com.unicam.dto.Risposte.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;

/**
 * Il rappresentante del comune, una volta fatto il log-in
 * vedrà tutte le proposte di eventi/contest da parte
 * dell'animatore del comune
 */
@RestController
@RequestMapping(name = "api/comune")
public class ComuneController {

    private ComuneService servizioComune;
    private UtenteService servizioUtente;
    /*@Autowired
    private ContenutoService<PuntoGeolocalizzato> servizioPuntoGeo;
    @Autowired
    private ContenutoService<PuntoLogico> servizioPuntoLo;
    @Autowired
    private ContenutoService<Itinerario> servizioIti;
    @Autowired
    private ContenutoService<Evento> servizioEv;
    @Autowired
    private ContenutoService<Contest> servizioCon;*/

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

    @Autowired
    public ComuneController(ComuneService servizio,
                            UtenteService service){
        this.servizioComune = servizio;
        this.servizioUtente = service;
    }

    @PostMapping("Api/Comune/RichiestaAggiunta")
    public void RichiestaAggiunta(@RequestBody RichiestaComuneDTO richiesta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        //prendo il comune dell'utente
        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.COMUNE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }
        RichiestaAggiuntaComune richiestaAggiunta = new RichiestaAggiuntaComune(servizioUtente,
                servizioPuntoGeo, servizioComune, richiesta, idUtente);
        richiestaAggiunta.Execute();

    }

    @GetMapping("Api/Comune/RicercaComune")
    public ResponseEntity<RicercaContenutiResponseDTO> RicercaComune(RicercaComuneDTO ricerca){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        //prendo il comune dell'utente
        String comune = userDetails.getComune();

        if(!this.servizioComune.ContainComune(ricerca.getNome().toUpperCase(Locale.ROOT)))
            throw new NullPointerException("Il comune cercato non è presente");

        this.servizioUtente.AggiornaComuneVisitato(idUtente, ricerca.getNome());

        RicercaContenutiResponseDTO ricercaComune = new RicercaContenutiResponseDTO();

        List<PuntoGeoResponseDTO> puntiGeolocalizzati = this.servizioPuntoGeo.GetPuntiGeoByComune(ricerca.getNome());
        List<PuntoLogicoResponseDTO> puntiLogici = this.servizioPuntoLo.GetPuntiLogiciByComune(ricerca.getNome());
        List<ItinerarioResponseDTO> itinerari = this.servizioIti.GetItinerariByComune(ricerca.getNome());
        List<EventoResponseDTO> eventi = this.servizioEv.GetEventiByComune(ricerca.getNome());
        List<ContestResponseDTO> contest = this.servizioCon.GetContestByComuneRuolo(ricerca.getNome(), Ruolo.TURISTA_AUTENTICATO);


        ricercaComune.getContenutiPresenti().put("punti geolocalizzati", puntiGeolocalizzati);
        ricercaComune.getContenutiPresenti().put("punti logici / avvisi", puntiLogici);
        ricercaComune.getContenutiPresenti().put("itinerari", itinerari);
        ricercaComune.getContenutiPresenti().put("eventi", eventi);
        ricercaComune.getContenutiPresenti().put("contest", contest);
        return ResponseEntity.ok(ricercaComune);

    }


    @GetMapping("Api/Comune/GetProposteAnimatore")
    public void GetProposteAnimatore(){
    }

    @PutMapping("Api/Comune/EsitoProposta")
    public void EsitoProposta(){
    }
}
