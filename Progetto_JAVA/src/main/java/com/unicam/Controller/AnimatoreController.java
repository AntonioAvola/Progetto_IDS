package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaContest;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaEvento;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.ContestService;
import com.unicam.Service.Contenuto.EventoService;
import com.unicam.Service.Contenuto.PuntoGeoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.ContestTerminatoDTO;
import com.unicam.dto.PropostaContestDTO;
import com.unicam.dto.PropostaEventoDTO;
import com.unicam.dto.Risposte.ContestVotiDTO;
import com.unicam.dto.Risposte.RicercaContenutiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(name = "api/animatore")
public class AnimatoreController {

    @Autowired
    private ComuneService servizioComune;
    private UtenteService servizioUtente;
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

        this.servizioComune.ControlloPresenzaComune(comune);

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

        this.servizioComune.ControlloPresenzaComune(comune);

        ControllaInizioFine(proposta.getInizio(), proposta.getFine());

        //controllo che non esiste già un evento nel database con lo stesso nome (indipendentemente dallo stato del contenuto)
        this.servizioContest.ControllaPresenzaNome(proposta.getTitolo().toUpperCase(Locale.ROOT), comune);

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

    @GetMapping("Api/Animatore/Contest-Terminati")
    public ResponseEntity<List<ContestVotiDTO>> ContestTerminati(){

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

        List<ContestVotiDTO> contestsFiniti = this.servizioContest.GetContestByComuneTempo(comune, adesso);

        return ResponseEntity.ok(contestsFiniti);
    }

    @GetMapping("Api/Animatore/Partecipanti-Contest-Terminato")
    public ResponseEntity<ContestTerminatoDTO> PartecipantiContest(@RequestParam String contest){
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

        this.servizioContest.ControllaPresenzaNomeApprovato(contest.toUpperCase(Locale.ROOT), comune);

        ContestTerminatoDTO contestTerminato = this.servizioContest.PartecipantiContest(contest.toUpperCase(Locale.ROOT), comune, adesso);

        return ResponseEntity.ok(contestTerminato);
    }

    @PutMapping("Api/Animatore/Assegna-Vincitore")
    public void AssegnaVincitore(@RequestParam String contest, @RequestParam String vincitore){

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

        this.servizioContest.AssegnaVincitore(contest.toUpperCase(Locale.ROOT), vincitore, adesso, comune);
    }
}
