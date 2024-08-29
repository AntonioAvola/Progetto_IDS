package com.unicam.Service;

import com.unicam.Authorization.AuthorizationService;
import com.unicam.Model.Contenuto;
import com.unicam.Model.User;
import com.unicam.Repository.ContenutoRepository;
import com.unicam.dto.UtenteDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ContenutoService <T extends Contenuto> {

    private ContenutoRepository repo;
    private AuthorizationService autorizzazioni = new AuthorizationService();


    @Autowired
    public ContenutoService(ContenutoRepository repo) {
        this.repo = repo;
    }

    public void AggiungiContenuto(User user, T contenuto){
        UtenteDTO utente = new UtenteDTO(user.getUsername(), user.getComune(), user.getRuolo());
        if(!autorizzazioni.VerificaPermesso(utente, "AggiungiContenuto")){
            throw new UnsupportedOperationException("Non hai il permesso di creare contenuti");
        }
        System.out.println("Contenuto aggiunto");
        repo.add(contenuto);
    }

    public void ApprovaContenuto(User user, T contenuto){
        UtenteDTO utente = new UtenteDTO(user.getUsername(), user.getComune(), user.getRuolo());
        if(!autorizzazioni.VerificaPermesso(utente, "ApprovaContenuto")){
            throw new UnsupportedOperationException("Non hai il permesso di validare contenuti");
        }
        // TODO implementare logica per approvare il contenuto
    }

}
