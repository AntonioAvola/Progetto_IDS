package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaContenuto;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.PropostaContestDTO;
import com.unicam.dto.PropostaEventoDTO;
import com.unicam.dto.Provvisori.PropostaContestProvvisoriaDTO;
import com.unicam.dto.Provvisori.PropostaEventoProvvisoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

/**
 * Metodi disponibili solo agli animatori
 */
@RestController
@RequestMapping(name = "api/animatore")
public class AnimatoreController {

    private UtenteService servizioUtente;
    private ContenutoService<Evento> servizioEvento;
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
    }

    @PostMapping("api/animatore/proponiEvento")
    public void ProponiEvento(@RequestBody PropostaEventoDTO proposta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        //prendo il comune dell'utente
        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.ANIMATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        Evento evento = proposta.ToEntity(this.servizioUtente.GetUtenteById(idUtente), comune);
        evento.setDurata(new Tempo(proposta.getInizio(), proposta.getFine()));
        evento.setLuogo(this.servizioPunto.GetPuntoByNome(proposta.getNomeLuogo()));
        RichiestaAggiuntaContenuto<Evento> richiesta = new RichiestaAggiuntaContenuto<>(servizioEvento, evento);
        richiesta.Execute();
    }

    @PostMapping("api/animatore/proponiContest")
    public void ProponiContest(@RequestBody PropostaContestDTO proposta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        //prendo il comune dell'utente
        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.ANIMATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }
        Contest contest = proposta.ToEntity(this.servizioUtente.GetUtenteById(idUtente), comune);
        RichiestaAggiuntaContenuto<Contest> richiesta = new RichiestaAggiuntaContenuto<>(servizioContest, contest);
        richiesta.Execute();
    }
}
