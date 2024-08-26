package com.unicam;

import com.unicam.Controller.ContenutoController;
import com.unicam.Model.*;
import com.unicam.Repository.ContenutoRepository;
import com.unicam.Service.AutorizzazioneService;

public class Main {
    public static void main(String[] args) {

        User utente = new User("Antonio", "antonio.avola02@gmail.com", "1234", "Castelfidardo", "anto2002");
        PuntoGeolocalizzato punto = new PuntoGeolocalizzato(40.24222, 23.453);
        Contenuto contenuto = new Contenuto(1L, "vacanza", "è stato bellissimo", TipoContenuto.IMMAGINE, utente, punto, StatoContenuto.IN_ATTESA);

        ContenutoRepository repository = new ContenutoRepository();



        AutorizzazioneService autorizzazioneService = new AutorizzazioneService();
        ContenutoController controller = new ContenutoController(autorizzazioneService, repository);


        try {
            controller.pubblicaContenuto(utente, contenuto);
        } catch (IllegalArgumentException e) {
            System.out.println("Errore: " + e.getMessage());
        }

        System.out.println("La lista di contenuti pubblicati:");
        for (Contenuto c : repository.findAll()) {
            System.out.println(c.getTitolo() + ": " + c.getDescrizione());
        }
    }



}
