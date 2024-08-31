package com.unicam.Controller;

import com.unicam.Model.Contenuto;
import com.unicam.Model.User;
import com.unicam.Service.ContenutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/contenuto")
public class ContenutoController {
    private ContenutoService servizio;
    //private AuthorizationService autorizzioneService;

    @Autowired
    public ContenutoController(ContenutoService servizio){
        this.servizio = servizio;
    }

    /*@PostMapping(path = "{id}/publica")
    public void pubblicaContenuto(@RequestBody Long id, Contenuto contenuto) {
        servizio.AggiungiContenuto(id, contenuto);
    }*/
}
