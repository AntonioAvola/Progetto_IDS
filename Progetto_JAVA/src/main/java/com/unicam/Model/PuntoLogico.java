package com.unicam.Model;

import com.unicam.dto.UtenteDTO;

public class PuntoLogico extends Contenuto{

    private PuntoGeolocalizzato riferimento;

    public PuntoLogico(String descrizione, UtenteDTO autore, PuntoGeolocalizzato punto){
        super("AVVISO", descrizione, autore);
        this.riferimento = punto;
    }


    public PuntoGeolocalizzato getRiferimento() {
        return riferimento;
    }
}
