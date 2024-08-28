package com.unicam;

import com.unicam.Controller.ContenutoController;
import com.unicam.Model.*;
import com.unicam.Model.BuilderContenuto.EventoBuilder;
import com.unicam.Model.BuilderContenuto.PuntoGeoBuilder;
import com.unicam.Repository.ContenutoRepository;
import com.unicam.Service.AutorizzazioneService;
import com.unicam.Service.ContenutoService;
import com.unicam.dto.UtenteDTO;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {

        ContenutoRepository contenutoRepo = new ContenutoRepository();

        ContenutoService<Evento> eventoService = new ContenutoService(contenutoRepo);

        UtenteDTO utente = new UtenteDTO("Antonio Avola", "Castelfidardo", Ruolo.CONTRIBUTOR_AUTORIZZATO);
        LocalDateTime inizio = LocalDateTime.of(2024,8,28,10,0);
        LocalDateTime fine = LocalDateTime.of(2024,8,28,12,0);
        Tempo durata = new Tempo(inizio, fine);

        PuntoGeoBuilder geoBuilder = new PuntoGeoBuilder();
        geoBuilder.BuildAutore(utente);
        geoBuilder.BuildTitolo("Spiaggia");
        geoBuilder.BuildDescrizione("spiaggia, vicino al mare");
        geoBuilder.BuildSpecifica(43.6167, 13.5167);
        PuntoMappaSemplice punto = new PuntoMappaSemplice();


        //Creo un nuovo evento
        EventoBuilder builder = new EventoBuilder();
        builder.BuildAutore(utente);
        builder.BuildDescrizione("Festa in spiaggia con tanta musica Dance!");
        builder.BuildSpecifica(durata,punto);
        Evento evento = builder.Result();

        eventoService.AggiungiContenuto(new User("Antonio", "antonio@domain.com", "password", "Castelfidardo", "ant_avola"), evento);
        System.out.println("Evento creato con successo: " + evento);

    }




}
