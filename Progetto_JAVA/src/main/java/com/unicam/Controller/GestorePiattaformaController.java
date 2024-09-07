package com.unicam.Controller;

import com.unicam.Model.Comune;
import com.unicam.Model.Ruolo;
import com.unicam.Model.StatoContenuto;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ComuneService;
import com.unicam.dto.AccettaRifiutaComuneDTO;
import com.unicam.dto.LoginDTO;
import com.unicam.dto.Risposte.ComuneResponseDTO;
import com.unicam.dto.Risposte.LoginResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(name = "Api/GestorePiattaforma")
public class GestorePiattaformaController {

    @Autowired
    private ComuneService servizioComune;

    @GetMapping("Api/GestorePiattaforma/RichiesteComuni")
    public ResponseEntity<List<ComuneResponseDTO>> RicercaRichiesteComune(){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String currentRole = userDetails.getRole();

        if(!currentRole.equals(Ruolo.ADMIN.name()))
            throw new IllegalArgumentException("Non hai i permessi per l'operazione");

        List<ComuneResponseDTO> comuniPresenti = this.servizioComune.GetComuneByStato(StatoContenuto.ATTESA);
        return ResponseEntity.ok(comuniPresenti);
    }

    @PutMapping("Api/GestorePiattaforma/AccettaRifiutaComune")
    public void AccettaORifiuta(@RequestBody AccettaRifiutaComuneDTO comune){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String currentRole = userDetails.getRole();

        if(!currentRole.equals(Ruolo.ADMIN.name()))
            throw new IllegalArgumentException("Non hai i permessi per l'operazione");

        this.servizioComune.AccettaORifiutaComune(comune.getNomeComune(), comune.getStato());
    }

}
