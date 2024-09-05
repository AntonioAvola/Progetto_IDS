package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaPost;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Contenuto.*;
import com.unicam.Service.PostService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.AggiungiPreferitoDTO;
import com.unicam.dto.PostTuristaDTO;

import com.unicam.dto.Provvisori.SegnalazioneProvvisoriaDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping(name = "api/turistaAutenticato")
public class TuristaAutenticatoController<T extends Contenuto> {

    private UtenteService servizioUtente;

    /*private ContenutoService<Itinerario> serviceItinerario;
    private ContenutoService<PuntoGeolocalizzato> servicePuntoGeo;
    private ContenutoService<PuntoLogico> servicePuntoLogico;
    @Autowired
    private ContenutoService<Evento> serviceEv;
    @Autowired
    private ContenutoService<Contest> serviceCon;
    private ContenutoService<PostTurista> servizioPost;

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
    }*/

    private ItinerarioService serviceItinerario;
    private PuntoGeoService servicePuntoGeo;
    private PuntoLogicoService servicePuntoLogico;
    @Autowired
    private EventoService serviceEv;
    @Autowired
    private ContestService serviceCon;
    private PostService servizioPost;

    public TuristaAutenticatoController(PostService servizioPost,
                                        ItinerarioService serviceIt,
                                        PuntoGeoService servicePG,
                                        PuntoLogicoService servicePL,
                                        UtenteService servizioUtente){
        this.serviceItinerario = serviceIt;
        this.servicePuntoGeo = servicePG;
        this.servicePuntoLogico = servicePL;
        this.servizioPost = servizioPost;
        this.servizioUtente = servizioUtente;
    }

    @PostMapping(value = "/aggiuntaPost")
    public void AggiungiPost(@RequestBody PostTuristaDTO UserFile, @RequestParam("contenutoMultimediale") MultipartFile file) throws IOException {
/**
 *  TODO REVIEW ele vedi la modifica?
 */
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        /**
         * Antonio, perchè il CONTRIBUTOR dovrebbe aggiungere un post?
         * Comunque il ruolo non viene mai cambiato a TURISTA_AUTENTICATO
         */
        if(this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato() == comune)
            throw new IllegalArgumentException("Non hai i permessi per inserire un post");

        RichiestaAggiuntaPost aggiunta = new RichiestaAggiuntaPost(servizioPost, servizioUtente, UserFile,this.servizioUtente.GetUtenteById(idUtente), comune);
        aggiunta.Execute();
        /*post.setStato(StatoContenuto.APPROVATO);
        servizioPost.AggiungiContenuto(post);*/
    }

    public void AggiungiPreferito(@RequestBody AggiungiPreferitoDTO preferito){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato() == comune)
            throw new IllegalArgumentException("Non hai i permessi per inserire un post");

        if(preferito.getTipoContenuto() == "punti geolocalizzati")
            this.servicePuntoGeo.AggiuntiPreferito(preferito.getNomeContenuto(), idUtente);
        else if(preferito.getTipoContenuto() == "punti logici"
                || preferito.getTipoContenuto() == "avvisi"
                || preferito.getTipoContenuto() == "punti logici / avvisi")
            this.servicePuntoLogico.AggiungiPreferito(preferito.getNomeContenuto(), idUtente);
        else if(preferito.getTipoContenuto() == "itinerari")
            this.serviceItinerario.AggiungiPreferito(preferito.getNomeContenuto(), idUtente);
        else if(preferito.getTipoContenuto() == "eventi")
            this.serviceEv.AggiungiPreferito(preferito.getNomeContenuto(), idUtente);
        else if(preferito.getTipoContenuto() == "contest")
            this.serviceCon.AggiungiPreferito(preferito.getNomeContenuto(), idUtente);
        else
            throw new IllegalArgumentException("Il tipo di contenuto non esiste. Oppure è stato scritto in maniera errata");
    }

    @PutMapping("/segnalaContenuto")
    public void SegnalaContenuto(@RequestBody SegnalazioneProvvisoriaDTO segnala){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato() == comune)
            throw new IllegalArgumentException("Non hai i permessi per inserire un post");

        if(segnala.getTipo().toUpperCase() == "ITINERARIO")
            this.serviceItinerario.SegnalaContenuto(segnala.getNomeContenuto(), segnala.getIdCreatore());
        else if(segnala.getTipo().toUpperCase() == "PUNTO GEOLOCALIZZATO")
            this.servicePuntoGeo.SegnalaContenuto(segnala.getNomeContenuto(), segnala.getIdCreatore());
        else if(segnala.getTipo().toUpperCase() == "PUNTO LOGICO")
            this.servicePuntoLogico.SegnalaContenuto(segnala.getNomeContenuto(), segnala.getIdCreatore());
        else
            throw new IllegalArgumentException("Non è possibile segnalare questo tipo di contenuto");
    }
}
