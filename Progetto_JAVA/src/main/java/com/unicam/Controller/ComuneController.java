package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaComune;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ComuneService;
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
import java.util.Map;

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
    @Autowired
    private ContenutoService<PuntoGeolocalizzato> servizioPuntoGeo;
    @Autowired
    private ContenutoService<PuntoLogico> servizioPuntoLo;
    @Autowired
    private ContenutoService<Itinerario> servizioIti;
    @Autowired
    private ContenutoService<Evento> servizioEv;
    @Autowired
    private ContenutoService<Contest> servizioCon;

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

    @GetMapping("Api/Comune/GetProposteAnimatore")
    public void GetProposteAnimatore(){
    }

    @PutMapping("Api/Comune/EsitoProposta")
    public void EsitoProposta(){
    }
}
