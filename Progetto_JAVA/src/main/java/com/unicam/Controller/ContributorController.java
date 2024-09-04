package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaContenuto;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.ItinerarioDTO;
import com.unicam.dto.PuntoGeoDTO;
import com.unicam.dto.PuntoLogicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

/**
 * I metodi di questa classe devono poter essere utilizzati da:
 * CONTRIBUTOR
 * CONTRIBUTOR_AUTORIZZATO
 * CURATORE
 */
@RestController
@RequestMapping(name = "Api/contributor")
public class ContributorController<T extends Contenuto> {

    private final SecurityAutoConfiguration securityAutoConfiguration;
    private UtenteService serviceUtente;
    private ContenutoService<Itinerario> serviceItinerario;
    private ContenutoService<PuntoGeolocalizzato> servicePuntoGeo;
    private ContenutoService<PuntoLogico> servicePuntoLogico;

    @Autowired
    public ContributorController(ContenutoService<Itinerario> serviceIt,
                                 ContenutoService<PuntoGeolocalizzato> servicePG,
                                 ContenutoService<PuntoLogico> servicePL,
                                 UtenteService servizioUtente,
                                 SecurityAutoConfiguration securityAutoConfiguration){
        this.serviceItinerario = serviceIt;
        this.servicePuntoGeo = servicePG;
        this.servicePuntoLogico = servicePL;
        this.serviceUtente = servizioUtente;
        this.securityAutoConfiguration = securityAutoConfiguration;
    }


    @PostMapping("Api/Contributor/AggiungiItinerario")
    public ResponseEntity<String> AggiungiItinerario(@RequestBody ItinerarioDTO richiesta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        //prendo il comune dell'utente
        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.CONTRIBUTOR.name()) &&
                !currentRole.equals(Ruolo.CONTRIBUTOR_AUTORIZZATO.name()) &&
                !currentRole.equals(Ruolo.CURATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        Itinerario itinerario = richiesta.ToEntity(this.serviceUtente.GetUtenteById(idUtente), comune);
        itinerario.setPuntiDiInteresse(serviceItinerario.GetPuntiByListaNomi(richiesta.getNomiPunti()));
        if(currentRole.equals(Ruolo.CONTRIBUTOR.name())){
            RichiestaAggiuntaContenuto<Itinerario> aggiunta =
                    new RichiestaAggiuntaContenuto<>(serviceItinerario, itinerario);
            aggiunta.Execute();
        }
        else{
            itinerario.setStato(StatoContenuto.APPROVATO);
            serviceItinerario.AggiungiContenuto(itinerario);
        }
        return ResponseEntity.ok("itinerario aggiunto con succeso!");
    }

    @PostMapping("Api/Contributor/AggiungiPuntoGeo")

    public void AggiungiPuntoGeolocalizzato(@RequestBody PuntoGeoDTO richiesta){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        //prendo il comune dell'utente
        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.CONTRIBUTOR.name()) &&
                !currentRole.equals(Ruolo.CONTRIBUTOR_AUTORIZZATO.name()) &&
                !currentRole.equals(Ruolo.CURATORE.name()) &&
                !currentRole.equals(Ruolo.ANIMATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        PuntoGeolocalizzato punto = richiesta.ToEntity(this.serviceUtente.GetUtenteById(idUtente), comune);
        if(currentRole.equals(Ruolo.CONTRIBUTOR.name())){
            RichiestaAggiuntaContenuto<PuntoGeolocalizzato> aggiunta =
                    new RichiestaAggiuntaContenuto<>(servicePuntoGeo, punto);
            aggiunta.Execute();
        }
        else{
            punto.setStato(StatoContenuto.APPROVATO);
            servicePuntoGeo.AggiungiContenuto(punto);
        }
    }

    @PostMapping("Api/Contributor/aggiungiPuntoLogico")
    public void AggiungiPuntoLogico(@RequestBody PuntoLogicoDTO richiesta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        //prendo il comune dell'utente
        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.CONTRIBUTOR.name()) &&
                !currentRole.equals(Ruolo.CONTRIBUTOR_AUTORIZZATO.name()) &&
                !currentRole.equals(Ruolo.CURATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        PuntoLogico punto = richiesta.ToEntity(this.serviceUtente.GetUtenteById(idUtente), comune);
        punto.setRiferimento(this.servicePuntoGeo.GetPuntoByNome(richiesta.getNomePuntoGeo()));
        if(currentRole.equals(Ruolo.CONTRIBUTOR.name())){
            RichiestaAggiuntaContenuto<PuntoLogico> aggiunta =
                    new RichiestaAggiuntaContenuto<>(servicePuntoLogico, punto);
            aggiunta.Execute();
        }
        else{
            punto.setStato(StatoContenuto.APPROVATO);
            servicePuntoLogico.AggiungiContenuto(punto);
        }
    }

    public void ModificaContenuto(){
        //TODO implement
    }

    public void EliminaContenuto(){
        //TODO implement
    }


}
