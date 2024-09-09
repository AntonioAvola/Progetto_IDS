package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaComune;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.*;
import com.unicam.Service.UtenteService;
import com.unicam.dto.AccettaRifiutaContenutoDTO;
import com.unicam.dto.Provvisori.ContenutoAttesaDTO;
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

import java.time.LocalDateTime;
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

    @PostMapping("Api/Comune/Richiesta-Inserimento-Comune")
    public void RichiestaAggiunta(@RequestBody RichiestaComuneDTO richiesta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.COMUNE.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
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

    @GetMapping("Api/Comune/Visita-Comune")
    public ResponseEntity<RicercaContenutiResponseDTO> VisitaComune(@RequestParam String ricerca){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String comune = ricerca.toUpperCase(Locale.ROOT);

        String currentRole = userDetails.getRole();

        if(currentRole.equals(Ruolo.ADMIN.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        ControlloPresenzaComune(comune);

        this.servizioUtente.AggiornaComuneVisitato(idUtente, comune);

        RicercaContenutiResponseDTO ricercaComune = new RicercaContenutiResponseDTO();

        LocalDateTime adesso = LocalDateTime.now();

        List<PuntoGeoResponseDTO> puntiGeolocalizzati = this.servizioPuntoGeo.GetPuntiGeoByComune(comune);
        List<PuntoLogicoResponseDTO> puntiLogici = this.servizioPuntoLo.GetPuntiLogiciByComune(comune);
        List<ItinerarioResponseDTO> itinerari = this.servizioIti.GetItinerariByComune(comune);
        List<EventoResponseDTO> eventi = this.servizioEv.GetEventiByComune(comune);
        List<ContestResponseDTO> contest;
        if(comune.equals(this.servizioUtente.GetUtenteById(idUtente).getComune()))
            contest = this.servizioCon.GetContestByComuneRuolo(comune, this.servizioUtente.GetUtenteById(idUtente).getRuoloComune(), adesso);
        else{
            contest = this.servizioCon.GetContestByComuneRuolo(comune, Ruolo.TURISTA_AUTENTICATO, adesso);
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


    @GetMapping("Api/Comune/Proposte-Animatore")
    public ResponseEntity<RicercaContenutiResponseDTO> RicercaProposteAnimatore(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(!currentRole.equals(Ruolo.COMUNE.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        LocalDateTime adesso = LocalDateTime.now();

        RicercaContenutiResponseDTO contenuti = new RicercaContenutiResponseDTO();
        List<EventoResponseDTO> eventi = this.servizioEv.GetEventiByComune(comune, StatoContenuto.ATTESA);
        List<ContestResponseDTO> contest = this.servizioCon.GetContestStatoByComune(comune, StatoContenuto.ATTESA, adesso);

        contenuti.getContenutiPresenti().put("eventi", eventi);
        contenuti.getContenutiPresenti().put("contest", contest);
        return ResponseEntity.ok(contenuti);
    }

    @PutMapping("Api/Comune/Accetta-Proposta")
    public void AccettaProposta(@RequestBody ContenutoAttesaDTO contenuto){

        String comune = ControllaPermessi();

        if(contenuto.getTipoContenuto().equals("eventi"))
            this.servizioEv.AccettaORifiuta(contenuto.getNomeContenuto().toUpperCase(Locale.ROOT), comune, StatoContenuto.APPROVATO);
        else if(contenuto.getTipoContenuto().equals("contest"))
            this.servizioCon.AccettaORifiuta(contenuto.getNomeContenuto().toUpperCase(Locale.ROOT), comune, StatoContenuto.APPROVATO);
        else
            throw new IllegalArgumentException("Il tipo di contenuto non esiste. Oppure è stato scritto in maniera errata");
    }

    @PutMapping("Api/Comune/Rifiuta-Proposta")
    public void RifiutaProposta(@RequestBody ContenutoAttesaDTO contenuto){

        String comune = ControllaPermessi();

        if(contenuto.getTipoContenuto().equals("eventi"))
            this.servizioEv.AccettaORifiuta(contenuto.getNomeContenuto().toUpperCase(Locale.ROOT), comune, StatoContenuto.RIFIUTATO);
        else if(contenuto.getTipoContenuto().equals("contest"))
            this.servizioCon.AccettaORifiuta(contenuto.getNomeContenuto().toUpperCase(Locale.ROOT), comune, StatoContenuto.RIFIUTATO);
        else
            throw new IllegalArgumentException("Il tipo di contenuto non esiste. Oppure è stato scritto in maniera errata");
    }

    private String ControllaPermessi() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(!currentRole.equals(Ruolo.COMUNE.name()))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        return comune;
    }
}
