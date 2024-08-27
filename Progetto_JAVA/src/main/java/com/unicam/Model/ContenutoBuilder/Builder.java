package com.unicam.Model.ContenutoBuilder;

import com.unicam.Model.Contenuto;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;

public abstract class Builder {

    protected Contenuto contenuto;

    public void BuildContenuto(Long id, String titolo, String descrizione, User autore, StatoContenuto stato){
        contenuto = new Contenuto(titolo, descrizione, stato);
    }
}
