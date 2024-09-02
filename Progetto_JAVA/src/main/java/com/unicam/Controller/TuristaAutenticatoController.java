package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaPost;
import com.unicam.Service.ContenutoService;
import com.unicam.dto.Provvisori.PostTuristaProvvisorioDTO;
import com.unicam.dto.Provvisori.SegnalazioneProvvisoriaDTO;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping(name = "api/turistaAutenticato")
public class TuristaAutenticatoController<T extends Contenuto> {

    private ContenutoService<Itinerario> serviceItinerario;
    private ContenutoService<PuntoGeolocalizzato> servicePuntoGeo;
    private ContenutoService<PuntoLogico> servicePuntoLogico;
    private ContenutoService<PostTurista> servizioPost;

    public TuristaAutenticatoController(ContenutoService<PostTurista> servizioPost,
                                        ContenutoService<Itinerario> serviceIt,
                                        ContenutoService<PuntoGeolocalizzato> servicePG,
                                        ContenutoService<PuntoLogico> servicePL){
        this.serviceItinerario = serviceIt;
        this.servicePuntoGeo = servicePG;
        this.servicePuntoLogico = servicePL;
        this.servizioPost = servizioPost;
    }

    @PostMapping("api/turistaAutenticato/aggiuntaPost")
    public void AggiungiPost(@RequestBody PostTuristaProvvisorioDTO richiesta){
        PostTurista post = null;
        try {
            post = richiesta.ToEntity();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        RichiestaAggiuntaPost aggiunta = new RichiestaAggiuntaPost(servizioPost, post);
        aggiunta.Execute();
    }

    @PutMapping("api/turistaAutenticato/segnalaContenuto")
    public void SegnalaContenuto(@RequestBody SegnalazioneProvvisoriaDTO segnala){
        if(segnala.getTipo().toUpperCase() == "ITINERARIO")
            this.serviceItinerario.SegnalaContenuto(segnala);
        else if(segnala.getTipo().toUpperCase() == "PUNTO GEOLOCALIZZATO")
            this.servicePuntoGeo.SegnalaContenuto(segnala);
        else if(segnala.getTipo().toUpperCase() == "PUNTO LOGICO")
            this.servicePuntoLogico.SegnalaContenuto(segnala);
        else
            throw new IllegalArgumentException("Non è possibile segnalare questo tipo di contenuto");
    }
}
