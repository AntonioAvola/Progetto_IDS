package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaComune;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.*;
import com.unicam.Service.UtenteService;
import com.unicam.dto.AccettaRifiutaContenutoDTO;
import com.unicam.dto.RicercaComuneDTO;
import com.unicam.dto.RichiestaComuneDTO;
import com.unicam.dto.Risposte.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
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

    private final SecurityAutoConfiguration securityAutoConfiguration;
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
                            UtenteService service,
                            SecurityAutoConfiguration securityAutoConfiguration){
        this.servizioComune = servizio;
        this.servizioUtente = service;
        this.securityAutoConfiguration = securityAutoConfiguration;
    }

    @PostMapping("Api/Comune/RichiestaAggiunta")
    public void RichiestaAggiunta(@RequestBody RichiestaComuneDTO richiesta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.COMUNE.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(this.servizioComune.ContainComune(comune)) {
            List<ComuneResponseDTO> approvati = this.servizioComune.GetComuneByStato(StatoContenuto.APPROVATO);
            for(ComuneResponseDTO comuneTrovato : approvati){
                if(comuneTrovato.getNome().equals(comune))
                    throw new IllegalArgumentException("Il comune è già stato inserito nel sistema");
            }
            throw new IllegalArgumentException("La richiesta di aggiunta del comune è già stata inoltrata");
        }

        RichiestaAggiuntaComune richiestaAggiunta = new RichiestaAggiuntaComune(servizioUtente,
                servizioPuntoGeo, servizioComune, richiesta, servizioPuntoGeo, idUtente);
        richiestaAggiunta.Execute();

    }

    @GetMapping("Api/Comune/VisitaComune")
    public ResponseEntity<RicercaContenutiResponseDTO> VisitaComune(@RequestParam String ricerca){


        //TODO rivedere
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String comune = ricerca.toUpperCase(Locale.ROOT);



        ControlloPresenzaComune(comune);

        this.servizioUtente.AggiornaComuneVisitato(idUtente, comune);

        RicercaContenutiResponseDTO ricercaComune = new RicercaContenutiResponseDTO();

        List<PuntoGeoResponseDTO> puntiGeolocalizzati = this.servizioPuntoGeo.GetPuntiGeoByComune(comune);
        List<PuntoLogicoResponseDTO> puntiLogici = this.servizioPuntoLo.GetPuntiLogiciByComune(comune);
        List<ItinerarioResponseDTO> itinerari = this.servizioIti.GetItinerariByComune(comune);
        List<EventoResponseDTO> eventi = this.servizioEv.GetEventiStatoByComune(comune);
        List<ContestResponseDTO> contest;
        if(comune.equals(this.servizioUtente.GetUtenteById(idUtente).getComune()))
            contest = this.servizioCon.GetContestByComuneRuolo(comune, this.servizioUtente.GetUtenteById(idUtente).getRuoloComune());
        else{
            contest = this.servizioCon.GetContestByComuneRuolo(comune, Ruolo.TURISTA_AUTENTICATO);
        }


        ricercaComune.getContenutiPresenti().put("punti geolocalizzati", puntiGeolocalizzati);
        ricercaComune.getContenutiPresenti().put("punti logici / avvisi", puntiLogici);
        ricercaComune.getContenutiPresenti().put("itinerari", itinerari);
        ricercaComune.getContenutiPresenti().put("eventi", eventi);
        ricercaComune.getContenutiPresenti().put("contest", contest);
        return ResponseEntity.ok(ricercaComune);

    }

    private void ControlloPresenzaComune(String comune) {
        if(!this.servizioComune.ContainComune(comune))
            throw new NullPointerException("Non è stata ancora fatta richiesta di inserimento del comune nel sistema");
        if(this.servizioComune.GetComuneByNome(comune).getStatoRichiesta() == StatoContenuto.ATTESA)
            throw new IllegalArgumentException("Il comune non è ancora stato accettato nel sistema");
    }


    @GetMapping("Api/Comune/GetProposteAnimatore")
    public ResponseEntity<RicercaContenutiResponseDTO> RicercaProposteAnimatore(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.COMUNE.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        RicercaContenutiResponseDTO contenuti = new RicercaContenutiResponseDTO();
        List<EventoResponseDTO> eventi = this.servizioEv.GetEventiStatoByComune(comune, StatoContenuto.ATTESA);
        List<ContestResponseDTO> contest = this.servizioCon.GetContestStatoByComune(comune, StatoContenuto.ATTESA);

        contenuti.getContenutiPresenti().put("eventi", eventi);
        contenuti.getContenutiPresenti().put("contest", contest);
        return ResponseEntity.ok(contenuti);
    }

    @PutMapping("Api/Comune/EsitoProposta")
    public void EsitoProposta(@RequestBody AccettaRifiutaContenutoDTO contenuto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.COMUNE.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(contenuto.getTipoContenuto().equals("eventi"))
            this.servizioEv.AccettaORifiuta(contenuto.getNomeContenuto().toUpperCase(Locale.ROOT), comune, contenuto.getStato());
        else if(contenuto.getTipoContenuto().equals("contest"))
            this.servizioCon.AccettaORifiuta(contenuto.getNomeContenuto().toUpperCase(Locale.ROOT), comune, contenuto.getStato());
        else
            throw new IllegalArgumentException("Il tipo di contenuto non esiste. Oppure è stato scritto in maniera errata");
    }
}
