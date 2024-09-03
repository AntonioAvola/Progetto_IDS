package com.unicam.Controller;

import com.unicam.Model.Comune;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.Ruolo;
import com.unicam.Richieste.RichiestaAggiuntaComune;
import com.unicam.Service.ComuneService;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.RichiestaComuneDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

/**
 * Il rappresentante del comune, una volta fatto il log-in
 * vedrà tutte le proposte di eventi/contest da parte
 * dell'animatore del comune
 */
@RestController
@RequestMapping(name = "api/comune")
public class ComuneController {

    private ComuneService servizioComune;
    private ContenutoService<PuntoGeolocalizzato> servizioPunto;
    private UtenteService servizioUtente;

    @Autowired
    public ComuneController(ComuneService servizio,
                            UtenteService service,
                            ContenutoService<PuntoGeolocalizzato> servizioPunto){
        this.servizioComune = servizio;
        this.servizioUtente = service;
        this.servizioPunto = servizioPunto;
    }

    @PostMapping("Api/Comune/RichiestaAggiunta")
    public void RichiestaAggiunta(@RequestBody RichiestaComuneDTO richiesta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentRole = authentication.getAuthorities().iterator().next().getAuthority();

        String idUtenteStr = authentication.getCredentials().toString();
        Long idUtente = Long.parseLong(idUtenteStr);

        if(!currentRole.equals(Ruolo.COMUNE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }


            RichiestaAggiuntaComune richiestaAggiunta = new RichiestaAggiuntaComune(servizioUtente, servizioPunto, servizioComune, richiesta);
            richiestaAggiunta.Execute();

    }

    @GetMapping("Api/Comune/GetProposteAnimatore")
    public void GetProposteAnimatore(){
    }

    @PutMapping("Api/Comune/EsitoProposta")
    public void EsitoProposta(){
    }
}
