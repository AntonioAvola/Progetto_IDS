package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaItinerario;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaPuntoGeo;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaPuntoLogico;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.*;
import com.unicam.Service.ProxyOSM.ProxyOSM;
import com.unicam.Service.UtenteService;
import com.unicam.dto.*;
import com.unicam.dto.OSM.PuntoGeoOSMDTO;
import com.unicam.dto.Provvisori.ContenutoAttesaDTO;
import com.unicam.dto.Risposte.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

/**
 * I metodi di questa classe devono poter essere utilizzati da:
 * CONTRIBUTOR
 * CONTRIBUTOR_AUTORIZZATO
 * CURATORE
 */
@RestController
@RequestMapping(name = "Api/contributor")
public class ContributorController {

    private final SecurityAutoConfiguration securityAutoConfiguration;

    @Autowired
    private ProxyOSM servizioMappa;
    @Autowired
    private UtenteService serviceUtente;
    @Autowired
    private ItinerarioService serviceItinerario;
    @Autowired
    private PuntoGeoService servicePuntoGeo;
    @Autowired
    private PuntoLogicoService servicePuntoLogico;
    @Autowired
    private EventoService serviceEvento;
    @Autowired
    private ContestService serviceContest;
    @Autowired
    private ComuneService serviceComune;

    @Autowired
    public ContributorController(SecurityAutoConfiguration securityAutoConfiguration){
        this.securityAutoConfiguration = securityAutoConfiguration;
    }

    @PostMapping("Api/Contributor-ContributorAutorizzato-Curatore/Aggiungi-Itinerario")
    public ResponseEntity<String> AggiungiItinerario(@RequestBody ItinerarioDTO richiesta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.serviceUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(!currentRole.equals(Ruolo.CONTRIBUTOR.name()) &&
                !currentRole.equals(Ruolo.CONTRIBUTOR_AUTORIZZATO.name()) &&
                !currentRole.equals(Ruolo.CURATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        this.serviceComune.ControlloPresenzaComune(comune);

        //controllo che non esista già un itinerario nel database con lo stesso nome (indipendentemente dallo stato del contenuto)
        this.serviceItinerario.ControllaPresenzaNome(richiesta.getTitolo().toUpperCase(Locale.ROOT), comune);

        Itinerario itinerario = richiesta.ToEntity(this.serviceUtente.GetUtenteById(idUtente), comune);
        itinerario.setPuntiDiInteresse(ControllaPuntiDiInteresse(richiesta.getNomiPunti(), comune));
        if(currentRole.equals(Ruolo.CONTRIBUTOR.name())){
            RichiestaAggiuntaItinerario aggiunta = new RichiestaAggiuntaItinerario(serviceItinerario, itinerario);
            aggiunta.Execute();
        }
        else{
            itinerario.setStato(StatoContenuto.APPROVATO);
            serviceItinerario.AggiungiContenuto(itinerario);
            this.serviceItinerario.EliminaItinerariCheSiRipetonoPerNomeOPunti(itinerario);
        }
        return ResponseEntity.ok("itinerario aggiunto con succeso!");
    }

    private List<PuntoGeolocalizzato> ControllaPuntiDiInteresse(List<String> nomiPunti, String comune) {
        return this.servicePuntoGeo.GetPuntiByListaNomiAndComuneAndStato(nomiPunti, comune);
    }

    @PostMapping("Api/Contributor-ContributorAutorizzato-Curatore-Animatore/Aggiungi-PuntoGeo")
    public void AggiungiPuntoGeolocalizzato(@RequestBody /*PuntoGeoDTO*/ PuntoGeoOSMDTO richiesta) throws IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.serviceUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(!currentRole.equals(Ruolo.CONTRIBUTOR.name()) &&
                !currentRole.equals(Ruolo.CONTRIBUTOR_AUTORIZZATO.name()) &&
                !currentRole.equals(Ruolo.CURATORE.name()) &&
                !currentRole.equals(Ruolo.ANIMATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        this.serviceComune.ControlloPresenzaComune(comune);

        //controllo che non esista già un punto logico nel database con lo stesso luogo (indipendentemente dallo stato del contenuto)
        this.servicePuntoGeo.ControllaPresenzaNome(richiesta.getTitolo().toUpperCase(Locale.ROOT), comune);

        String indirizzo = richiesta.getTitolo();
        String comuneRigerimento = URLEncoder.encode(comune, StandardCharsets.UTF_8.toString());
        indirizzo = URLEncoder.encode(indirizzo, StandardCharsets.UTF_8.toString());
        List<Double> coordinate = servizioMappa.getCoordinates(indirizzo  + "," + comuneRigerimento);

        PuntoGeoDTO puntoTrovato = new PuntoGeoDTO(richiesta.getTitolo(), richiesta.getDescrizione(), coordinate.get(0), coordinate.get(1));

        PuntoGeolocalizzato punto = puntoTrovato.ToEntity(this.serviceUtente.GetUtenteById(idUtente), comune);

        if(currentRole.equals(Ruolo.CONTRIBUTOR.name())){
            RichiestaAggiuntaPuntoGeo aggiunta = new RichiestaAggiuntaPuntoGeo(servicePuntoGeo, punto);
            aggiunta.Execute();
        }
        else{
            punto.setStato(StatoContenuto.APPROVATO);
            servicePuntoGeo.AggiungiContenuto(punto);
            this.servicePuntoGeo.EliminaContenutiAttesaDoppioni(punto);
        }
    }

    @PostMapping("Api/Contributor-ContributorAutorizzato-Curatore/Aggiungi-PuntoLogico")
    public void AggiungiPuntoLogico(@RequestBody PuntoLogicoDTO richiesta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.serviceUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(!currentRole.equals(Ruolo.CONTRIBUTOR.name()) &&
                !currentRole.equals(Ruolo.CONTRIBUTOR_AUTORIZZATO.name()) &&
                !currentRole.equals(Ruolo.CURATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        this.serviceComune.ControlloPresenzaComune(comune);

        //controllo che non esista già un punto geolocalizzato nel database con lo stesso nome (indipendentemente dallo stato del contenuto)
        this.servicePuntoLogico.ControllaPresenzaNome(richiesta.getTitolo().toUpperCase(Locale.ROOT), richiesta.getNomePuntoGeo(), comune);

        PuntoLogico punto = richiesta.ToEntity(this.serviceUtente.GetUtenteById(idUtente), comune);
        punto.setRiferimento(this.servicePuntoGeo.GetPuntoGeoByNomeAndComuneAndStato(richiesta.getNomePuntoGeo(), comune));
        if(currentRole.equals(Ruolo.CONTRIBUTOR.name())){
            RichiestaAggiuntaPuntoLogico aggiunta = new RichiestaAggiuntaPuntoLogico(servicePuntoLogico, punto);
            aggiunta.Execute();
        }
        else{
            punto.setStato(StatoContenuto.APPROVATO);
            servicePuntoLogico.AggiungiContenuto(punto);
            this.servicePuntoLogico.EliminaContenutiAttesaDoppioni(punto);
        }
    }

    @GetMapping("Api/Utente/Tutti-I-Propri-Contenuti")
    public ResponseEntity<RicercaContenutiResponseDTO> PropriContenuti(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.serviceUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(currentRole.equals(Ruolo.ADMIN.name()) ||
                currentRole.equals(Ruolo.COMUNE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        this.serviceComune.ControlloPresenzaComune(comune);

        RicercaContenutiResponseDTO propriContenutiApprovati = new RicercaContenutiResponseDTO();

        if(currentRole.equals(Ruolo.CONTRIBUTOR.name()) || currentRole.equals(Ruolo.CONTRIBUTOR_AUTORIZZATO.name()) || currentRole.equals(Ruolo.CURATORE.name())){
            List<PuntoLogicoResponseDTO> puntiLogici = this.servicePuntoLogico.GetPuntiLogiciByAutore(this.serviceUtente.GetUtenteById(idUtente));
            List<ItinerarioResponseDTO> itinerari = this.serviceItinerario.GetItinerarioByAutore(this.serviceUtente.GetUtenteById(idUtente));
            propriContenutiApprovati.getContenutiPresenti().put("punti logici / avvisi", puntiLogici);
            propriContenutiApprovati.getContenutiPresenti().put("itinerari", itinerari);
        }
        else{
            List<EventoResponseDTO> eventi = this.serviceEvento.GetEventiByAutore(this.serviceUtente.GetUtenteById(idUtente));
            List<ContestResponseDTO> contest = this.serviceContest.GetContestByAutore(this.serviceUtente.GetUtenteById(idUtente));
            propriContenutiApprovati.getContenutiPresenti().put("eventi", eventi);
            propriContenutiApprovati.getContenutiPresenti().put("contest", contest);
        }
        List<PuntoGeoResponseDTO> puntiGeolocalizzati = this.servicePuntoGeo.GetPuntiGeoByAutore(this.serviceUtente.GetUtenteById(idUtente));
        propriContenutiApprovati.getContenutiPresenti().put("punti geolocalizzati", puntiGeolocalizzati);


        return ResponseEntity.ok(propriContenutiApprovati);
    }

    @GetMapping("Api/Utente/Contest-Vinti")
    public ResponseEntity<List<EsitoContestDTO>> EsitoContest(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.serviceUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(currentRole.equals(Ruolo.ADMIN.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        if(this.serviceUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune) &&
                (currentRole.equals(Ruolo.ANIMATORE.name()) || currentRole.equals(Ruolo.COMUNE.name())))
            throw new IllegalArgumentException("Non partecipando ai contest non puoi vedere se hai vinto o meno");

        LocalDateTime adesso = LocalDateTime.now();

        String username = this.serviceUtente.GetUtenteById(idUtente).getUsername();
        List<EsitoContestDTO> esito = this.serviceContest.EsitoContest(username, adesso);

        return ResponseEntity.ok(esito);
    }

    @DeleteMapping("Api/Utente/Elimina-Proprio-PuntoGeo-Itinerario-Evento-Contest")
    public void EliminaGeoItineraioEventoContest(@RequestBody ContenutoAttesaDTO contenuto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.serviceUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(currentRole.equals(Ruolo.ADMIN.name()))
            throw new IllegalArgumentException("ADMIN; nessun contenuto da eliminare");

        if(currentRole.equals(Ruolo.COMUNE.name()))
            throw new IllegalArgumentException("COMUNE; nessun contenuto da visualizzare oltre al proprio punto geolocalizzato");

        if(contenuto.getTipoContenuto().equals("itinerari"))
            this.serviceItinerario.EliminaItinerario(contenuto.getNomeContenuto(), comune);
        else if(contenuto.getTipoContenuto().equals("punti geolocalizzati"))
            this.servicePuntoGeo.EliminaPuntoGeo(contenuto.getNomeContenuto(), comune);
        else if(contenuto.getTipoContenuto().equals("punti logici") || contenuto.getTipoContenuto().equals("avvisi") ||
                contenuto.getTipoContenuto().equals("punti logici / avvisi"))
            throw new IllegalArgumentException("Non è possibile eliminare questo tipo di contenuto in questa API." );
        else if(contenuto.getTipoContenuto().equals("eventi"))
            this.serviceEvento.EliminaEvento(contenuto.getNomeContenuto(), comune);
        else if(contenuto.getTipoContenuto().equals("contest"))
            this.serviceContest.EliminaContest(contenuto.getNomeContenuto(), comune);
        else
            throw new IllegalArgumentException("Il tipo di contenuto non esiste. Oppure è stato scritto in maniera errata. " +
                    "Si possono segnalare i punti geolocalizzati e gli itinerari");
    }

    @DeleteMapping("Api/Utente/Elimina-Proprio-PuntoLogico")
    public void EliminaPuntoLogico(@RequestBody AccettaRifiutaPuntoLogicoDTO contenuto){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.serviceUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(currentRole.equals(Ruolo.ADMIN.name()))
            throw new IllegalArgumentException("ADMIN; nessun contenuto da eliminare");

        String titoloAvviso = contenuto.getTitoloAvviso().toUpperCase(Locale.ROOT);

        if(!titoloAvviso.contains("AVVISO!!"))
            titoloAvviso = "AVVISO!! " + titoloAvviso;
        if(!this.servicePuntoLogico.ContienePuntoLogico(titoloAvviso, comune))
            throw new IllegalArgumentException("Il punto logico/avviso specificato non esiste. Controllare di aver scritto correttamente il titolo");

        this.servicePuntoLogico.EliminaPuntoLogico(titoloAvviso, comune, contenuto.getNomeLuogo());
    }
}
