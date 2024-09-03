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
import org.springframework.web.bind.annotation.*;

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
        if(this.servizioUtente.GetUtenteById(richiesta.getIdResponsabile()).getRuolo() == Ruolo.COMUNE){
            RichiestaAggiuntaComune richiestaAggiunta = new RichiestaAggiuntaComune(servizioUtente, servizioPunto, servizioComune, richiesta);
            richiestaAggiunta.Execute();
        }
        else{
            throw new IllegalArgumentException("Non si può richiedere di aggiungere un comune");
        }
    }

    @GetMapping("Api/Comune/GetProposteAnimatore")
    public void GetProposteAnimatore(){
    }

    @PutMapping("Api/Comune/EsitoProposta")
    public void EsitoProposta(){
    }
}
