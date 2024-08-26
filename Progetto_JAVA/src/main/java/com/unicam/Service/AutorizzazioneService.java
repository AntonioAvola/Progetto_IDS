package com.unicam.Service;

import com.unicam.Model.Ruolo;
import com.unicam.Model.User;

public class AutorizzazioneService implements IAutorizzazioneService{
    @Override
    public boolean PubblicaContenuto(User utente) {
        Ruolo ruolo = utente.getRuolo();
        return ruolo == Ruolo.CONTRIBUTOR_AUTORIZZATO || ruolo == Ruolo.CURATORE || ruolo == Ruolo.ANIMATORE || ruolo == Ruolo.TURISTA_AUTENTICATO;
    }
    @Override
    public boolean validaContenuto(User utente){
        Ruolo ruolo = utente.getRuolo();
        return ruolo == Ruolo.CURATORE;
    }
    @Override
    public boolean eliminaContenuto(User utente){
        Ruolo ruolo = utente.getRuolo();
        return ruolo == Ruolo.CURATORE || ruolo == Ruolo.CONTRIBUTOR || ruolo == Ruolo.CONTRIBUTOR_AUTORIZZATO
                || ruolo == Ruolo.TURISTA_AUTENTICATO || ruolo == Ruolo.ANIMATORE;
    }
    @Override
    public boolean modificaContenuto(User utente){
        Ruolo ruolo = utente.getRuolo();
        return ruolo == Ruolo.CURATORE || ruolo == Ruolo.CONTRIBUTOR || ruolo == Ruolo.CONTRIBUTOR_AUTORIZZATO
                || ruolo == Ruolo.TURISTA_AUTENTICATO || ruolo == Ruolo.ANIMATORE;
    }
    @Override
    public boolean proponiContenuto(User utente){
        Ruolo ruolo = utente.getRuolo();
        return ruolo == Ruolo.CURATORE || ruolo == Ruolo.CONTRIBUTOR || ruolo == Ruolo.CONTRIBUTOR_AUTORIZZATO
                || ruolo == Ruolo.TURISTA_AUTENTICATO || ruolo == Ruolo.ANIMATORE;
    }
    @Override
    public boolean proponiContest(User utente){
        Ruolo ruolo = utente.getRuolo();
        return ruolo == Ruolo.CURATORE || ruolo == Ruolo.CONTRIBUTOR || ruolo == Ruolo.CONTRIBUTOR_AUTORIZZATO
                || ruolo == Ruolo.TURISTA_AUTENTICATO;
    }




}
