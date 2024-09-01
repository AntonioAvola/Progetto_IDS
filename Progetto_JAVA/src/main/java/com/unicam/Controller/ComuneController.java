package com.unicam.Controller;

import com.unicam.Service.ComuneService;
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

    private ComuneService servizio;

    @Autowired
    public ComuneController(ComuneService servizio){
        this.servizio = servizio;
    }

    @PostMapping("Api/Comune/RichiestaAggiunta")
    public void RichiestaAggiunta(@RequestBody RichiestaComuneDTO richiesta){
    }

    @GetMapping("Api/Comune/GetProposteAnimatore")
    public void GetProposteAnimatore(){
    }

    @PutMapping("Api/Comune/EsitoProposta")
    public void EsitoProposta(){
    }
}
