package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Contenuto.*;
import com.unicam.Service.UtenteService;
import com.unicam.dto.AccettaRifiutaPuntoLogicoDTO;
import com.unicam.dto.Provvisori.ContenutoAttesaDTO;
import com.unicam.dto.Risposte.ItinerarioResponseDTO;
import com.unicam.dto.Risposte.PuntoGeoResponseDTO;
import com.unicam.dto.Risposte.PuntoLogicoResponseDTO;
import com.unicam.dto.Risposte.RicercaContenutiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping(name = "Api/Curatore")
public class CuratoreController {

    /*@Autowired
    private ContenutoService<PuntoGeolocalizzato> servizioPuntoGeo;
    @Autowired
    private ContenutoService<PuntoLogico> servizioPuntoLo;
    @Autowired
    private ContenutoService<Itinerario> servizioIti;
    @Autowired
    private ContenutoService<Evento> servizioEv;
    @Autowired
    private ContenutoService<Contest> servizioCon;*/

    @Autowired
    private PuntoGeoService servizioPuntoGeo;
    @Autowired
    private PuntoLogicoService servizioPuntoLo;
    @Autowired
    private ItinerarioService servizioIti;
    @Autowired
    private EventoService servizioEv;
    @Autowired
    private ContestService servizioCon;
    @Autowired
    private UtenteService servizioUtente;

    @GetMapping("Api/Curatore/Contenuti-In-Attesa")
    public ResponseEntity<RicercaContenutiResponseDTO> RicercaContenutiAttesa(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(!currentRole.equals(Ruolo.CURATORE.name()))
            throw new IllegalArgumentException("Non hai i permessi per effettuare la ricerca");

        return ResponseEntity.ok(ContenutiTrovati(comune, StatoContenuto.ATTESA));
    }

    private RicercaContenutiResponseDTO ContenutiTrovati(String comune, StatoContenuto stato) {
        RicercaContenutiResponseDTO contenuti = new RicercaContenutiResponseDTO();
        List<PuntoGeoResponseDTO> puntiGeo = this.servizioPuntoGeo.GetPuntiGeoStatoByComune(comune, stato);
        List<PuntoLogicoResponseDTO> puntiLogici = this.servizioPuntoLo.GetPuntiLogiciStatoByComune(comune, stato);
        List<ItinerarioResponseDTO> itinerari = this.servizioIti.GetItinerariStatoByComune(comune, stato);

        contenuti.getContenutiPresenti().put("punti geolocalizzati", puntiGeo);
        contenuti.getContenutiPresenti().put("punti logici / avvisi", puntiLogici);
        contenuti.getContenutiPresenti().put("itinerari", itinerari);
        //TODO da aggiungere i post del turista autenticato
        return contenuti;
    }

    @GetMapping("Api/Comune/Contenuti-Segnalati")
    public ResponseEntity<RicercaContenutiResponseDTO> RicercaSegnalazioni(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(!currentRole.equals(Ruolo.CURATORE.name()))
            throw new IllegalArgumentException("Non hai i permessi per effettuare la ricerca");

        return ResponseEntity.ok(ContenutiTrovati(comune, StatoContenuto.SEGNALATO));
    }

    @PutMapping("Api/Curatore/Accetta-PuntiGeo-Itinerari")
    public void AccettaContenuto(@RequestBody ContenutoAttesaDTO contenuto){

        String comune = ControlliPermessi();

        if(contenuto.getTipoContenuto().equals("punti geolocalizzati"))
            this.servizioPuntoGeo.AccettaORifiuta(contenuto.getNomeContenuto(), comune, StatoContenuto.APPROVATO);
        else if(contenuto.getTipoContenuto().equals("itinerari"))
            this.servizioIti.AccettaORifiuta(contenuto.getNomeContenuto(), comune, StatoContenuto.APPROVATO);
            //TODO aggiungere i post del turista autenticato
        else
            throw new IllegalArgumentException("Il tipo di contenuto non esiste. Oppure è stato scritto in maniera errata");
    }

    @PutMapping("Api/Curatore/Rifiuta-PuntiGeo-Itinerari")
    public void RifiutaContenuto(@RequestBody ContenutoAttesaDTO contenuto){

        String comune = ControlliPermessi();

        if(contenuto.getTipoContenuto().equals("punti geolocalizzati"))
            this.servizioPuntoGeo.AccettaORifiuta(contenuto.getNomeContenuto(), comune, StatoContenuto.RIFIUTATO);
        else if(contenuto.getTipoContenuto().equals("itinerari"))
            this.servizioIti.AccettaORifiuta(contenuto.getNomeContenuto(), comune, StatoContenuto.RIFIUTATO);
            //TODO aggiungere i post del turista autenticato
        else
            throw new IllegalArgumentException("Il tipo di contenuto non esiste. Oppure è stato scritto in maniera errata");
    }

    @PutMapping("Api/Curatore/Accetta-PuntiLogici-Avvisi")
    public void AccettaPuntoLogico(@RequestBody AccettaRifiutaPuntoLogicoDTO punto){

        String comune = ControlliPermessi();

        String titolo = ControllaAvviso(punto.getTitoloAvviso(), comune);

        this.servizioPuntoLo.AccettaORifiuta(titolo, punto.getNomeLuogo(), comune, StatoContenuto.APPROVATO);
    }

    @PutMapping("Api/Curatore/Rifiuta-PuntiLogici-Avvisi")
    public void RifiutaPuntoLogico(@RequestBody AccettaRifiutaPuntoLogicoDTO punto){

        String comune = ControlliPermessi();

        String titolo = ControllaAvviso(punto.getTitoloAvviso(), comune);
        this.servizioPuntoLo.AccettaORifiuta(titolo, punto.getNomeLuogo(), comune, StatoContenuto.RIFIUTATO);
    }


    private String ControlliPermessi() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        //controllo che l'utente non tenti di eseguire l'azione mentre si trova in un comune diverso dal suo, quindi quando è un turista autenticato
        if(!this.servizioUtente.GetUtenteById(idUtente).getComuneVisitato().equals(comune))
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");

        if(!currentRole.equals(Ruolo.CURATORE.name()))
            throw new IllegalArgumentException("Non hai i permessi per accettare o rifiutare contenuti");
        return comune;
    }

    private String ControllaAvviso(String titoloAvviso, String comune) {
        if(!titoloAvviso.contains("AVVISO!!"))
            titoloAvviso = "AVVISO!! " + titoloAvviso;
        if(!this.servizioPuntoLo.ContienePuntoLogico(titoloAvviso, comune))
            throw new IllegalArgumentException("Il punto logico/avviso specificato non esiste. Controllare di aver scritto correttamente il titolo");
        return titoloAvviso;
    }
}
