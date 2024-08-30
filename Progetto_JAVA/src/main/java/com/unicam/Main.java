package com.unicam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);


        /*
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
        */
    }




}
