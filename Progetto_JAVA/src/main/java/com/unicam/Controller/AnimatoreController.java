package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.EventoRepository;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaContest;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaEvento;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.ContestService;
import com.unicam.Service.Contenuto.EventoService;
import com.unicam.Service.Contenuto.PuntoGeoService;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.PropostaContestDTO;
import com.unicam.dto.PropostaEventoDTO;
import com.unicam.dto.Risposte.ContestVotiDTO;
import com.unicam.dto.Risposte.RicercaContenutiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Metodi disponibili solo agli animatori
 */
@RestController
@RequestMapping(name = "api/animatore")
public class AnimatoreController {

    @Autowired
    private ComuneService servizioComune;
    private UtenteService servizioUtente;
    /*private ContenutoService<Evento> servizioEvento;
    private ContenutoService<Contest> servizioContest;
    private ContenutoService<PuntoGeolocalizzato> servizioPunto;

    @Autowired
    public AnimatoreController(UtenteService servizioUtente,
                               ContenutoService<Evento> servizioEvento,
                               ContenutoService<Contest> servizioContest,
                               ContenutoService<PuntoGeolocalizzato> servizioPunto){
        this.servizioUtente = servizioUtente;
        this.servizioEvento = servizioEvento;
        this.servizioContest = servizioContest;
        this.servizioPunto = servizioPunto;
    }*/
    private EventoService servizioEvento;
    private ContestService servizioContest;
    private PuntoGeoService servizioPunto;

    @Autowired
    public AnimatoreController(UtenteService servizioUtente,
                               EventoService servizioEvento,
                               ContestService servizioContest,
                               PuntoGeoService servizioPunto){
        this.servizioUtente = servizioUtente;
        this.servizioEvento = servizioEvento;
        this.servizioContest = servizioContest;
        this.servizioPunto = servizioPunto;
    }

    @PostMapping("Api/Animatore/Proponi-Evento")
    public void ProponiEvento(@RequestBody PropostaEventoDTO proposta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(!currentRole.equals(Ruolo.ANIMATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        ControllaInizioFine(proposta.getInizio(), proposta.getFine());

        ControlloPresenzaComune(comune);

        //controllo che non esiste già un evento nel database con lo stesso nome (indipendentemente dallo stato del contenuto)
        this.servizioEvento.ControllaPresenzaNome(proposta.getTitolo().toUpperCase(Locale.ROOT), comune);

        this.servizioEvento.ControllaPresenzaNome(proposta.getTitolo(), comune);
        Evento evento = proposta.ToEntity(this.servizioUtente.GetUtenteById(idUtente), comune);
        evento.setDurata(new Tempo(proposta.getInizio(), proposta.getFine()));
        evento.setLuogo(ControlloPresenzaPuntoGeo(proposta.getNomeLuogo(), comune));
        RichiestaAggiuntaEvento richiesta = new RichiestaAggiuntaEvento(servizioEvento, evento);
        richiesta.Execute();
    }

    private PuntoGeolocalizzato ControlloPresenzaPuntoGeo(String nomeLuogo, String comune) {
        return this.servizioPunto.GetPuntoGeoByNomeAndComuneAndStato(nomeLuogo.toUpperCase(Locale.ROOT), comune);
    }

    private void ControlloPresenzaComune(String comune) {
        if(!this.servizioComune.ContainComune(comune))
            throw new NullPointerException("Non è stata ancora fatta richiesta di inserimento del comune nel sistema");
        if(this.servizioComune.GetComuneByNome(comune).getStatoRichiesta() == StatoContenuto.ATTESA)
            throw new IllegalArgumentException("Il comune non è ancora stato accettato nel sistema");
    }

    @PostMapping("Api/Animatore/Proponi-Contest")
    public void ProponiContest(@RequestBody PropostaContestDTO proposta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(!currentRole.equals(Ruolo.ANIMATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        ControllaInizioFine(proposta.getInizio(), proposta.getFine());

        //controllo che non esiste già un evento nel database con lo stesso nome (indipendentemente dallo stato del contenuto)
        this.servizioContest.ControllaPresenzaNome(proposta.getTitolo().toUpperCase(Locale.ROOT), comune, proposta.getInizio());

        Contest contest = proposta.ToEntity(this.servizioUtente.GetUtenteById(idUtente), comune);
        RichiestaAggiuntaContest richiesta = new RichiestaAggiuntaContest(servizioContest, contest);
        richiesta.Execute();
    }

    private void ControllaInizioFine(LocalDateTime inizio, LocalDateTime fine) {
        if(inizio.isAfter(fine))
            throw new IllegalArgumentException("La data di inizio è successiva alla data di fine");
        if(inizio.isEqual(fine))
            throw new IllegalArgumentException("Inizio e fine coincidono");

    }


    @GetMapping("Api/Animatore/Andamento-Contest")
    public ResponseEntity<RicercaContenutiResponseDTO> EsitoContestTerminati(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(!currentRole.equals(Ruolo.ANIMATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        LocalDateTime adesso = LocalDateTime.now();

        RicercaContenutiResponseDTO contestConVoti = new RicercaContenutiResponseDTO();

        List<ContestVotiDTO> contestInCorso = this.servizioContest.GetContestByComuneTempo(comune, adesso, false);
        List<ContestVotiDTO> contestsFinti = this.servizioContest.GetContestByComuneTempo(comune, adesso, true);

        contestConVoti.getContenutiPresenti().put("contest terminati:", contestsFinti);
        contestConVoti.getContenutiPresenti().put("contest in corso:", contestInCorso);

        return ResponseEntity.ok(contestConVoti);
    }
}
