package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaContenuto;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.ItinerarioDTO;
import com.unicam.dto.Provvisori.ItinerarioProvvisorioDTO;
import com.unicam.dto.Provvisori.PuntoGeoProvvisorioDTO;
import com.unicam.dto.Provvisori.PuntoLogicoProvvisorioDTO;
import com.unicam.dto.PuntoLogicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(name = "Api/contributor")
public class ContributorController<T extends Contenuto> {

    private UtenteService serviceUtente;
    private ContenutoService<Itinerario> serviceItinerario;
    private ContenutoService<PuntoGeolocalizzato> servicePuntoGeo;
    private ContenutoService<PuntoLogico> servicePuntoLogico;

    @Autowired
    public ContributorController(ContenutoService<Itinerario> serviceIt,
                                 ContenutoService<PuntoGeolocalizzato> servicePG,
                                 ContenutoService<PuntoLogico> servicePL,
                                 UtenteService servizio){
        this.serviceItinerario = serviceIt;
        this.servicePuntoGeo = servicePG;
        this.servicePuntoLogico = servicePL;
        this.serviceUtente = servizio;
    }

    @PostMapping("Api/Contributor/AggiungiItinerario")
    public void AggiungiItinerario(@RequestBody ItinerarioProvvisorioDTO richiesta){
        //TODO controlli che l'utente abbia l'autorizzazione
        User utente = this.serviceUtente.GetUtenteById(richiesta.getIdUtente());
        Itinerario itinerario = richiesta.ToEntity();
        itinerario.setPuntiDiInteresse(serviceItinerario.GetPuntiByListaNomi(richiesta.getNomiPunti()));
        if(utente.getRuolo() == Ruolo.CONTRIBUTOR){
            RichiestaAggiuntaContenuto<Itinerario> aggiunta = new RichiestaAggiuntaContenuto<>(serviceItinerario, itinerario);
            aggiunta.Execute();
        }
        else{
            serviceItinerario.AggiungiContenuto(itinerario);
        }
    }

    @PostMapping("Api/Contributor/AggiungiPuntoGeo")
    public void AggiungiPuntoGeolocalizzato(@RequestBody PuntoGeoProvvisorioDTO richiesta){
        //TODO controlli che l'utente abbia l'autorizzazione
        User utente = this.serviceUtente.GetUtenteById(richiesta.getIdUtente());
        PuntoGeolocalizzato punto = richiesta.ToEntity();
        if(utente.getRuolo() == Ruolo.CONTRIBUTOR){
            RichiestaAggiuntaContenuto<PuntoGeolocalizzato> aggiunta = new RichiestaAggiuntaContenuto<>(servicePuntoGeo, punto);
            aggiunta.Execute();
        }
        else{
            servicePuntoGeo.AggiungiContenuto(punto);
        }
    }

    @PostMapping("Api/Contributor/aggiungiPuntoLogico")
    public void AggiungiPuntoLogico(@RequestBody PuntoLogicoProvvisorioDTO richiesta){
        //TODO controlli che l'utente abbia l'autorizzazione
        User utente = this.serviceUtente.GetUtenteById(richiesta.getIdUtente());
        PuntoLogico punto = richiesta.ToEntity();
        punto.setRiferimento(servicePuntoGeo.GetPuntoByNome(richiesta.getNomePuntoGeo()));
        if(utente.getRuolo() == Ruolo.CONTRIBUTOR){
            RichiestaAggiuntaContenuto<PuntoLogico> aggiunta = new RichiestaAggiuntaContenuto<>(servicePuntoLogico, punto);
            aggiunta.Execute();
        }
        else{
            servicePuntoLogico.AggiungiContenuto(punto);
        }
    }

    public void ModificaContenuto(){
    }

    public void EliminaContenuto(){
    }


}
