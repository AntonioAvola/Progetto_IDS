package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Contenuto.*;
import com.unicam.dto.AccettaRifiutaContenutoDTO;
import com.unicam.dto.Risposte.ItinerarioResponseDTO;
import com.unicam.dto.Risposte.PuntoGeoResponseDTO;
import com.unicam.dto.Risposte.PuntoLogicoResponseDTO;
import com.unicam.dto.Risposte.RicercaContenutiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("Api/Curatore/ContenutiAttesa")
    public ResponseEntity<RicercaContenutiResponseDTO> RicercaContenutiAttesa(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

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

    @GetMapping("Api/Comune/SegnalazioniDiContenuti")
    public ResponseEntity<RicercaContenutiResponseDTO> RicercaSegnalazioni(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.CURATORE.name()))
            throw new IllegalArgumentException("Non hai i permessi per effettuare la ricerca");

        return ResponseEntity.ok(ContenutiTrovati(comune, StatoContenuto.SEGNALATO));
    }

    @PutMapping("Api/Curatore/AccettaORifiutaContenuti")
    public void AccettaORifiuta(@RequestBody AccettaRifiutaContenutoDTO contenuto){

        //TODO da modificare (al momento per punti geolocalizzati, punti logici, itinerari, eventi e contest)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.CURATORE.name()))
            throw new IllegalArgumentException("Non hai i permessi per accettare o rifiutare contenuti");

        if(contenuto.getTipoContenuto().equals("punti geolocalizzati"))
            this.servizioPuntoGeo.AccettaORifiuta(contenuto.getNomeContenuto(), comune, contenuto.getStato());
        else if(contenuto.getTipoContenuto().equals("punti logici")
                || contenuto.getTipoContenuto().equals("avvisi")
                || contenuto.getTipoContenuto().equals("punti logici / avvisi"))
            this.servizioPuntoLo.AccettaORifiuta(contenuto.getNomeContenuto(), comune, contenuto.getStato());
        else if(contenuto.getTipoContenuto().equals("itinerari"))
            this.servizioIti.AccettaORifiuta(contenuto.getNomeContenuto(), comune, contenuto.getStato());
        //TODO aggiungere i post del turista autenticato
        else
            throw new IllegalArgumentException("Il tipo di contenuto non esiste. Oppure è stato scritto in maniera errata");
    }
}
