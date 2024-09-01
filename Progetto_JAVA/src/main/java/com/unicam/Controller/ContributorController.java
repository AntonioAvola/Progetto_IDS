package com.unicam.Controller;

import com.unicam.Model.*;
import com.unicam.Richieste.RichiestaAggiuntaContenuto;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.ItinerarioDTO;
import com.unicam.dto.PuntoGeoDTO;
import com.unicam.dto.PuntoLogicoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

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
    public void AggiungiItinerario(@RequestBody Long idUtente, @RequestBody ItinerarioDTO richiesta){
        //TODO controlli che l'utente abbia l'autorizzazione
        User utente = this.serviceUtente.GetUtenteById(idUtente);
        Itinerario itinerario = richiesta.ToEntity();
        itinerario.setAutoreId(idUtente);
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
    public void AggiungiPuntoGeolocalizzato(@RequestBody Long idUtente, @RequestBody PuntoGeoDTO richiesta){
        //TODO controlli che l'utente abbia l'autorizzazione
        User utente = this.serviceUtente.GetUtenteById(idUtente);
        PuntoGeolocalizzato punto = richiesta.ToEntity();
        punto.setAutoreId(idUtente);
        if(utente.getRuolo() == Ruolo.CONTRIBUTOR){
            RichiestaAggiuntaContenuto<PuntoGeolocalizzato> aggiunta = new RichiestaAggiuntaContenuto<>(servicePuntoGeo, punto);
            aggiunta.Execute();
        }
        else{
            servicePuntoGeo.AggiungiContenuto(punto);
        }
    }

    @PostMapping("Api/Contributor/aggiungiPuntoLogico")
    public void AggiungiPuntoLogico(@RequestBody Long idUtente, @RequestBody PuntoLogicoDTO richiesta){
        //TODO controlli che l'utente abbia l'autorizzazione
        User utente = this.serviceUtente.GetUtenteById(idUtente);
        PuntoLogico punto = richiesta.ToEntity();
        punto.setAutoreId(idUtente);
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
