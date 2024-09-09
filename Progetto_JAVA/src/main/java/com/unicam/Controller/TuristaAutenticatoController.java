package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaPost;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.*;
import com.unicam.Service.PostService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.AggiungiPreferitoDTO;
import com.unicam.dto.PostTuristaDTO;

import com.unicam.dto.Provvisori.SegnalazioneProvvisoriaDTO;
import com.unicam.dto.Risposte.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;

@RestController
@RequestMapping(name = "api/turistaAutenticato")
public class TuristaAutenticatoController<T extends Contenuto> {

    private final SecurityAutoConfiguration securityAutoConfiguration;

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
    @Autowired
    private ComuneService servizioComune;
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
                                        UtenteService servizioUtente,
                                        SecurityAutoConfiguration securityAutoConfiguration){
        this.serviceItinerario = serviceIt;
        this.servicePuntoGeo = servicePG;
        this.servicePuntoLogico = servicePL;
        this.servizioPost = servizioPost;
        this.servizioUtente = servizioUtente;
        this.securityAutoConfiguration = securityAutoConfiguration;
    }

    @GetMapping("Api/Utente/Tutti-I-Comuni-Presenti-Nel-Sistema")
    public ResponseEntity<List<ComuneResponseDTO>> RicercaComuniPresenti(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String currentRole = userDetails.getRole();

        return ResponseEntity.ok(this.servizioComune.GetAllPresenti());

    }

    @PutMapping("Api/Turista/Aggiungi-A-Preferiti")
    public void AggiungiPreferito(@RequestBody AggiungiPreferitoDTO preferito){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        Comune comune = this.servizioComune.GetComuneByNome(this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato());

        if(this.servizioUtente.GetUtenteById(idUtente).getComune().equals(comune.getNome()))
            throw new IllegalArgumentException("Non hai i permessi per salvare tra i preferiti");

        if(preferito.getTipoContenuto().equals("punti geolocalizzati"))
            this.servicePuntoGeo.AggiuntiPreferito(preferito.getNomeContenuto(), comune.getNome(),  idUtente);
        else if(preferito.getTipoContenuto().equals("itinerari"))
            this.serviceItinerario.AggiungiPreferito(preferito.getNomeContenuto(), comune.getNome(), idUtente);
        else if(preferito.getTipoContenuto().equals("eventi"))
            this.serviceEv.AggiungiPreferito(preferito.getNomeContenuto(), comune.getNome(), idUtente);
        else if(preferito.getTipoContenuto().equals("contest"))
            this.serviceCon.AggiungiPreferito(preferito.getNomeContenuto(), comune.getNome(), idUtente);
        else if(preferito.getTipoContenuto().equals("punti logici") ||
                preferito.getTipoContenuto().equals("avvisi") ||
                preferito.getTipoContenuto().equals("punti logici / avvisi"))
            throw new IllegalArgumentException("Non è possibile inserire questo tipo di contenuto nei preferiti");
        else
            throw new IllegalArgumentException("Il tipo di contenuto non esiste. Oppure è stato scritto in maniera errata");
    }

    @PutMapping("Api/Turista/Segnala-PuntoGeo-Itinerario")
    public void SegnalaContenuto(@RequestBody SegnalazioneProvvisoriaDTO segnala){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        Comune comune = this.servizioComune.GetComuneByNome(this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato());

        String tipoContenuto = segnala.getTipo().toLowerCase(Locale.ROOT);

        if(this.servizioUtente.GetUtenteById(idUtente).getComune().equals(comune.getNome()))
            throw new IllegalArgumentException("Non hai i permessi per salvare tra i preferiti");

        if(tipoContenuto.equals("itinerari"))
            this.serviceItinerario.SegnalaContenuto(segnala.getNomeContenuto().toUpperCase(Locale.ROOT), comune.getNome());
        else if(tipoContenuto.equals("punti geolocalizzati"))
            this.servicePuntoGeo.SegnalaContenuto(segnala.getNomeContenuto().toUpperCase(Locale.ROOT), comune.getNome());
        else if(tipoContenuto.equals("eventi"))
            throw new IllegalArgumentException("Non è possibile segnalare questo tipo di contenuto. " +
                    "Si possono segnalare i punti geolocalizzati e gli itinerari");
        else if(tipoContenuto.equals("punti logici") ||
                tipoContenuto.equals("avvisi") ||
                tipoContenuto.equals("punti logici / avvisi"))
            throw new IllegalArgumentException("Non è possibile segnalare questo tipo di contenuto. " +
                    "Si possono segnalare i punti geolocalizzati e gli itinerari");
        else
            throw new IllegalArgumentException("Il tipo di contenuto non esiste. Oppure è stato scritto in maniera errata. " +
                    "Si possono segnalare i punti geolocalizzati e gli itinerari");
    }

    @GetMapping("Api/Turista/Tutti-I-Miei-Preferiti")
    public ResponseEntity<RicercaContenutiResponseDTO> TuttiMieiPreferiti(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        Comune comune = this.servizioComune.GetComuneByNome(this.servizioUtente.GetUtenteById(idUtente).getComune());

        if(this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune.getNome()))
            throw new IllegalArgumentException("Non hai i permessi per salvare tra i preferiti");

        RicercaContenutiResponseDTO preferiti = new RicercaContenutiResponseDTO();

        LocalDateTime adesso = LocalDateTime.now();

        List<PuntoGeoResponseDTO> puntiGeo = this.servicePuntoGeo.GetPuntiPreferiti(idUtente, comune.getNome());
        List<ItinerarioResponseDTO> itinerari = this.serviceItinerario.GetItinerariPreferiti(idUtente, comune.getNome());
        List<EventoResponseDTO> eventi = this.serviceEv.GetEventiPreferiti(idUtente, comune.getNome());
        List<ContestResponseDTO> contest = this.serviceCon.GetContestPreferiti(idUtente, comune.getNome(), adesso);

        preferiti.getContenutiPresenti().put("punti geolocalizzati", puntiGeo);
        preferiti.getContenutiPresenti().put("itinerari", itinerari);
        preferiti.getContenutiPresenti().put("eventi", eventi);
        preferiti.getContenutiPresenti().put("contest", contest);

        return ResponseEntity.ok(preferiti);
    }

    @PutMapping("Api/Partecipante-Contest/Voto-Favore")
    public void VotoAFavore(@RequestParam String nomeContest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        Comune comune = this.servizioComune.GetComuneByNome(this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato());

        if(this.servizioUtente.GetUtenteById(idUtente).getComune().equals(comune.getNome()))
            throw new IllegalArgumentException("Non hai i permessi per salvare tra i preferiti");

        this.serviceCon.ControllaPresenzaNomeApprovato(nomeContest.toUpperCase(Locale.ROOT), comune.getNome());

        this.serviceCon.PartecipaContest(nomeContest.toUpperCase(Locale.ROOT), comune.getNome(),true, idUtente);
    }

    @PutMapping("Api/Partecipante-Contest/Voto-Contrario")
    public void VotoContrario(@RequestParam String nomeContest){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        Comune comune = this.servizioComune.GetComuneByNome(this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato());

        if(this.servizioUtente.GetUtenteById(idUtente).getComune().equals(comune.getNome()))
            throw new IllegalArgumentException("Non hai i permessi per salvare tra i preferiti");

        this.serviceCon.ControllaPresenzaNomeApprovato(nomeContest.toUpperCase(Locale.ROOT), comune.getNome());

        this.serviceCon.PartecipaContest(nomeContest.toUpperCase(Locale.ROOT), comune.getNome(),false, idUtente);
    }
}
