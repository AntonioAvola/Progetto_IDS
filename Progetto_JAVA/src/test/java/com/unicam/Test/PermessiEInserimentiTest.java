package com.unicam.Test;

import com.unicam.Model.BuilderContenuto.ItinerarioBuilder;
import com.unicam.Model.Itinerario;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.Ruolo;
import com.unicam.Model.User;
import com.unicam.Repository.IContenutoRepository;
import com.unicam.Repository.UtenteRepository;
import com.unicam.Service.ContenutoService;
import com.unicam.dto.UtenteDTO;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

public class PermessiEInserimentiTest {

    private IContenutoRepository repositoryContenuto;

    private UtenteRepository repositoryUtente;
    @Test
    public void testAggiuntaContenutoFallita(){
        User utente = new User("Eleonora", "ele.car@gmail.com", "EleCar3",
                "Santa Vittoria", "ele-3-car");
        utente.setRuoloComune(Ruolo.CONTRIBUTOR);
        UtenteDTO autore = new UtenteDTO(utente.getName(), utente.getComune(), utente.getRuoloComune());

        List<PuntoGeolocalizzato> punti = new ArrayList<PuntoGeolocalizzato>();
        punti.add(new PuntoGeolocalizzato());

        ItinerarioBuilder builder = new ItinerarioBuilder();
        //builder.BuildAutore(utente.getId());
        builder.BuildTitolo("Corsa");
        builder.BuildDescrizione("Corsetta semplice in mezzo alla natura");
        builder.BuildSpecifica(punti);
        Itinerario itinerio = builder.Result();

        ContenutoService<Itinerario> service = new ContenutoService<>(repositoryContenuto, repositoryUtente);

        assertThrows(UnsupportedOperationException.class, () -> {
            service.AggiungiContenuto(itinerio);
        });
    }

    @Test
    public void testAggiuntaContenuto(){
        User utente = new User("Antonio", "anto.avo@gmail.com", "AntoAvo2",
                "Castelfidardo", "anto-2-avo");
        utente.setRuoloComune(Ruolo.CONTRIBUTOR_AUTORIZZATO);
        UtenteDTO autore = new UtenteDTO(utente.getName(), utente.getComune(), utente.getRuoloComune());

        List<PuntoGeolocalizzato> punti = new ArrayList<PuntoGeolocalizzato>();
        punti.add(new PuntoGeolocalizzato());

        ItinerarioBuilder builder = new ItinerarioBuilder();
        //builder.BuildAutore(utente.getId());
        builder.BuildTitolo("Corsa");
        builder.BuildDescrizione("Corsetta semplice in mezzo alla natura");
        builder.BuildSpecifica(punti);
        Itinerario itinerio = builder.Result();

        ContenutoService<Itinerario> service = new ContenutoService<>(repositoryContenuto, repositoryUtente);

        service.AggiungiContenuto(itinerio);
    }

}
