package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaPost;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.PostService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.AggiungiPreferitoDTO;
import com.unicam.dto.PostTuristaDTO;

import com.unicam.dto.Provvisori.PostTuristaProvvisorioDTO;
import com.unicam.dto.Provvisori.SegnalazioneProvvisoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Locale;

@RestController
@RequestMapping(name = "api/turistaAutenticato")
public class TuristaAutenticatoController<T extends Contenuto> {

    private ContenutoService<Itinerario> serviceItinerario;
    private ContenutoService<PuntoGeolocalizzato> servicePuntoGeo;
    private ContenutoService<PuntoLogico> servicePuntoLogico;
    @Autowired
    private ContenutoService<Evento> serviceEv;
    @Autowired
    private ContenutoService<Contest> serviceCon;
    private ContenutoService<PostTurista> servizioPost;
    private UtenteService servizioUtente;

    public TuristaAutenticatoController(ContenutoService<PostTurista> servizioPost,
                                        ContenutoService<Itinerario> serviceIt,
                                        ContenutoService<PuntoGeolocalizzato> servicePG,
                                        ContenutoService<PuntoLogico> servicePL,
                                        UtenteService servizioUtente){
        this.serviceItinerario = serviceIt;
        this.servicePuntoGeo = servicePG;
        this.servicePuntoLogico = servicePL;
        this.servizioPost = servizioPost;
        this.servizioUtente = servizioUtente;
    }

    @PostMapping(value = "/aggiuntaPost")
    public void AggiungiPost(@RequestParam("file") PostTuristaDTO UserFile) throws IOException {

        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        //prendo il comune dell'utente
        String comune = userDetails.getComune();

        if(this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato() == comune){
            throw new IllegalArgumentException("Non hai i permessi per inserire un post");
        }
        RichiestaAggiuntaPost richiesta = new RichiestaAggiuntaPost(servizioPost, servizioUtente, UserFile, this.servizioUtente.GetUtenteById(idUtente));
        return ResponseEntity.ok("Post inserito");*/
    }

    /*@PutMapping("/segnalaContenuto")
    public void SegnalaContenuto(@RequestBody SegnalazioneProvvisoriaDTO segnala){
        if(segnala.getTipo().toUpperCase() == "ITINERARIO")
            this.serviceItinerario.SegnalaContenuto(segnala);
        else if(segnala.getTipo().toUpperCase() == "PUNTO GEOLOCALIZZATO")
            this.servicePuntoGeo.SegnalaContenuto(segnala);
        else if(segnala.getTipo().toUpperCase() == "PUNTO LOGICO")
            this.servicePuntoLogico.SegnalaContenuto(segnala);
        else
            throw new IllegalArgumentException("Non è possibile segnalare questo tipo di contenuto");
    }*/
}
