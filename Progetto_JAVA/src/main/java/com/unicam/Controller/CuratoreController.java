package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.Contenuto.*;
import com.unicam.Service.ContenutoService;
import com.unicam.dto.Risposte.ItinerarioResponseDTO;
import com.unicam.dto.Risposte.PuntoGeoResponseDTO;
import com.unicam.dto.Risposte.PuntoLogicoResponseDTO;
import com.unicam.dto.Risposte.RicercaContenutiResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        //prendo il comune dell'utente
        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.CURATORE.name()))
            throw new IllegalArgumentException("Non hai i permessi per effettuare la ricerca");

        RicercaContenutiResponseDTO contenuti = new RicercaContenutiResponseDTO();

        List<PuntoGeoResponseDTO> puntiGeo = this.servizioPuntoGeo.GetPuntiGeoAttesaByComune(comune);
        List<PuntoLogicoResponseDTO> puntiLogici = this.servizioPuntoLo.GetPuntiLogiciAttesaByComune(comune);
        List<ItinerarioResponseDTO> itinerari = this.servizioIti.GetItinerariAttesaByComune(comune);

        contenuti.getContenutiPresenti().put("punti geolocalizzati", puntiGeo);
        contenuti.getContenutiPresenti().put("punti logici / avvisi", puntiLogici);
        contenuti.getContenutiPresenti().put("itinerari", itinerari);
        return ResponseEntity.ok(contenuti);
    }

    public void RicervaSegnalazioni(){

    }

    public void AccettaORifiuta(){

    }
}
