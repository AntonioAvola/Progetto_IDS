package com.unicam.Controller;

import com.unicam.Model.Contest;
import com.unicam.Model.Evento;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.Tempo;
import com.unicam.Richieste.RichiestaAggiuntaContenuto;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.Provvisori.PropostaContestProvvisoriaDTO;
import com.unicam.dto.Provvisori.PropostaEventoProvvisoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public void ProponiEvento(@RequestBody PropostaEventoProvvisoriaDTO proposta){
        Evento evento = proposta.ToEntity();
        evento.setDurata(new Tempo(proposta.getInizio(), proposta.getFine()));
        evento.setLuogo(this.servizioPunto.GetPuntoByNome(proposta.getNomeLuogo()));
        RichiestaAggiuntaContenuto<Evento> richiesta = new RichiestaAggiuntaContenuto<>(servizioEvento, evento);
        richiesta.Execute();
    }

    @PostMapping("api/animatore/proponiContest")
    public void ProponiContest(@RequestBody PropostaContestProvvisoriaDTO proposta){
        Contest contest = proposta.ToEntity();
        RichiestaAggiuntaContenuto<Contest> richiesta = new RichiestaAggiuntaContenuto<>(servizioContest, contest);
        richiesta.Execute();
    }
}
