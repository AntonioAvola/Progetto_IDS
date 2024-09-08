package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Repository.Contenuto.EventoRepository;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaContest;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaEvento;
import com.unicam.Richieste.RichiestaAggiuntaContenuto;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.ContestService;
import com.unicam.Service.Contenuto.EventoService;
import com.unicam.Service.Contenuto.PuntoGeoService;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.PropostaContestDTO;
import com.unicam.dto.PropostaEventoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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

    @PostMapping("api/animatore/proponiEvento")
    public void ProponiEvento(@RequestBody PropostaEventoDTO proposta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.ANIMATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        if(!this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        ControlloPresenzaComune(comune);

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

    @PostMapping("api/animatore/proponiContest")
    public void ProponiContest(@RequestBody PropostaContestDTO proposta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.ANIMATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        if(!this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        Contest contest = proposta.ToEntity(this.servizioUtente.GetUtenteById(idUtente), comune);
        ControllaNomeContest(contest.getTitolo(), contest.getComune());
        RichiestaAggiuntaContest richiesta = new RichiestaAggiuntaContest(servizioContest, contest);
        richiesta.Execute();
    }

    private void ControllaNomeContest(String titolo, String comune) {
        this.servizioContest.ControllaPresenzaNome(titolo.toUpperCase(Locale.ROOT), comune);
    }

    //TODO da implementare
    public void EsitoContest(){}
}
