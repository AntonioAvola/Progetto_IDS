package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaItinerario;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaPuntoGeo;
import com.unicam.Richieste.Contenuto.RichiestaAggiuntaPuntoLogico;
import com.unicam.Richieste.RichiestaAggiuntaContenuto;
import com.unicam.Security.UserCustomDetails;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.ItinerarioService;
import com.unicam.Service.Contenuto.PuntoGeoService;
import com.unicam.Service.Contenuto.PuntoLogicoService;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.ItinerarioDTO;
import com.unicam.dto.PuntoGeoDTO;
import com.unicam.dto.PuntoLogicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    /*private ContenutoService<Itinerario> serviceItinerario;
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
    }*/

    private ItinerarioService serviceItinerario;
    private PuntoGeoService servicePuntoGeo;
    private PuntoLogicoService servicePuntoLogico;
    @Autowired
    private ComuneService serviceComune;

    @Autowired
    public ContributorController(ItinerarioService serviceIt,
                                 PuntoGeoService servicePG,
                                 PuntoLogicoService servicePL,
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

        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.CONTRIBUTOR.name()) &&
                !currentRole.equals(Ruolo.CONTRIBUTOR_AUTORIZZATO.name()) &&
                !currentRole.equals(Ruolo.CURATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        ControlloPresenzaComune(comune);

        Itinerario itinerario = richiesta.ToEntity(this.serviceUtente.GetUtenteById(idUtente), comune);
        itinerario.setPuntiDiInteresse(ControllaPuntiDiInteresse(richiesta.getNomiPunti(), comune));
        if(currentRole.equals(Ruolo.CONTRIBUTOR.name())){
            RichiestaAggiuntaItinerario aggiunta = new RichiestaAggiuntaItinerario(serviceItinerario, itinerario);
            aggiunta.Execute();
        }
        else{
            itinerario.setStato(StatoContenuto.APPROVATO);
            serviceItinerario.AggiungiContenuto(itinerario);
            this.serviceItinerario.EliminaItinerariCheSiRipetonoPerNomeOPunti(itinerario);
        }
        return ResponseEntity.ok("itinerario aggiunto con succeso!");
    }

    private List<PuntoGeolocalizzato> ControllaPuntiDiInteresse(List<String> nomiPunti, String comune) {
        return this.servicePuntoGeo.GetPuntiByListaNomiAndComuneAndStato(nomiPunti, comune);
    }

    private void ControlloPresenzaComune(String comune) {
        if(!this.serviceComune.ContainComune(comune))
            throw new NullPointerException("Non è stata ancora fatta richiesta di inserimento del comune nel sistema");
        if(this.serviceComune.GetComuneByNome(comune).getStatoRichiesta() == StatoContenuto.ATTESA)
            throw new IllegalArgumentException("Il comune non è ancora stato accettato nel sistema");
    }

    @PostMapping("Api/Contributor/AggiungiPuntoGeo")

    public void AggiungiPuntoGeolocalizzato(@RequestBody PuntoGeoDTO richiesta){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.CONTRIBUTOR.name()) &&
                !currentRole.equals(Ruolo.CONTRIBUTOR_AUTORIZZATO.name()) &&
                !currentRole.equals(Ruolo.CURATORE.name()) &&
                !currentRole.equals(Ruolo.ANIMATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        ControlloPresenzaComune(comune);

        PuntoGeolocalizzato punto = richiesta.ToEntity(this.serviceUtente.GetUtenteById(idUtente), comune);
        if(currentRole.equals(Ruolo.CONTRIBUTOR.name())){
            RichiestaAggiuntaPuntoGeo aggiunta = new RichiestaAggiuntaPuntoGeo(servicePuntoGeo, punto);
            aggiunta.Execute();
        }
        else{
            punto.setStato(StatoContenuto.APPROVATO);
            servicePuntoGeo.AggiungiContenuto(punto);
            this.servicePuntoGeo.EliminaContenutiAttesaDoppioni(punto);
        }
    }

    @PostMapping("Api/Contributor/aggiungiPuntoLogico")
    public void AggiungiPuntoLogico(@RequestBody PuntoLogicoDTO richiesta){

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        UserCustomDetails userDetails = (UserCustomDetails) authentication.getPrincipal();

        String idUtenteStr = userDetails.getUserId();
        Long idUtente = Long.parseLong(idUtenteStr);

        String currentRole = userDetails.getRole();

        String comune = userDetails.getComune();

        if(!currentRole.equals(Ruolo.CONTRIBUTOR.name()) &&
                !currentRole.equals(Ruolo.CONTRIBUTOR_AUTORIZZATO.name()) &&
                !currentRole.equals(Ruolo.CURATORE.name())){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "non hai i permessi necessari per effettuare questa azione");
        }

        ControlloPresenzaComune(comune);


        PuntoLogico punto = richiesta.ToEntity(this.serviceUtente.GetUtenteById(idUtente), comune);
        punto.setRiferimento(ControlloPresenzaPuntoGeo(richiesta.getNomePuntoGeo(), comune));
        if(currentRole.equals(Ruolo.CONTRIBUTOR.name())){
            RichiestaAggiuntaPuntoLogico aggiunta = new RichiestaAggiuntaPuntoLogico(servicePuntoLogico, punto);
            aggiunta.Execute();
        }
        else{
            punto.setStato(StatoContenuto.APPROVATO);
            servicePuntoLogico.AggiungiContenuto(punto);
            this.servicePuntoLogico.EliminaContenutiAttesaDoppioni(punto);
        }
    }

    private PuntoGeolocalizzato ControlloPresenzaPuntoGeo(String nomePuntoGeo, String comune) {
        return this.servicePuntoGeo.GetPuntoGeoByNomeAndComuneAndStato(nomePuntoGeo, comune);
    }

    public void ModificaContenuto(){}

    public void EliminaContenuto(){
        //TODO implementare
    }


}
