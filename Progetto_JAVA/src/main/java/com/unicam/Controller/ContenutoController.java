package com.unicam.Controller;

import com.unicam.Model.Contenuto;
import com.unicam.Model.User;
import com.unicam.Repository.ContenutoRepository;
import com.unicam.Service.AutorizzazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class ContenutoController {
    private ContenutoRepository repository;
    private AutorizzazioneService autorizzioneService = new AutorizzazioneService();

    @Autowired
    public ContenutoController(AutorizzazioneService autorizzioneService, ContenutoRepository repository){
        this.repository = repository;
        this.autorizzioneService = autorizzioneService;
    }

    public void pubblicaContenuto(User utente, Contenuto contenuto) {
        if(autorizzioneService.PubblicaContenuto(utente)){
            System.out.println("Contenuto pubblicato con successo!");
            System.out.println("---------------------------------------");
            repository.add(contenuto);

        }else{
            throw new IllegalArgumentException("l'utente non ha il permesso di pubblicare contenuti");
        }
    }
}
