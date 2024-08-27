package com.unicam.Service;

import com.unicam.Authorization.AuthorizationService;
import com.unicam.Model.Contenuto;
import com.unicam.Model.User;
import com.unicam.Repository.ContenutoRepository;

public class ContenutoService <T extends Contenuto> {

    private ContenutoRepository repo;
    private AuthorizationService autorizzazioni = new AuthorizationService();

    public void AggiungiContenuto(User utente, T contenuto){
        if(autorizzazioni.verificaPermesso(utente, "AggiungiContenuto")){
            throw new UnsupportedOperationException("Non hai il permesso di creare contenuti");
        }
        repo.add(contenuto);
    }

    public void approvaContenuto(User utente, T contenuto){
        if(!autorizzazioni.verificaPermesso(utente, "ApprovaContenuto")){
            throw new UnsupportedOperationException("Non hai il permesso di validare contenuti");
        }
        // TODO implementare logica per approvare il contenuto
    }
}
