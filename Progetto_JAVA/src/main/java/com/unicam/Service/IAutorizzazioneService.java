package com.unicam.Service;

import com.unicam.Model.User;

public interface IAutorizzazioneService {

    boolean PubblicaContenuto(User utente);

    boolean validaContenuto(User utente);

    boolean eliminaContenuto(User utente);

    boolean modificaContenuto(User utente);

    boolean proponiContenuto(User utente);

    boolean proponiContest(User utente);
}
