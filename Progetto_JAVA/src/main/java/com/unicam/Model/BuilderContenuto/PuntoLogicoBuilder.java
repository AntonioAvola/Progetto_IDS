package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.PuntoLogico;
import com.unicam.Model.User;
import com.unicam.dto.UtenteDTO;

public class PuntoLogicoBuilder implements Builder{
    private PuntoLogico punto;

    public PuntoLogicoBuilder(){
        this.punto = new PuntoLogico();
    }

    @Override
    public void BuildAutore(UtenteDTO autore) {
        this.punto.setAutore(autore);
    }

    @Override
    public void BuildTitolo(String titolo) {
        this.punto.setTitolo("AVVISO!! " + titolo);
    }

    @Override
    public void BuildDescrizione(String descrizione) {
        this.punto.setDescrizione(descrizione);
    }

    public void BuildSpecifica(PuntoGeolocalizzato punto){
        this.punto.setRiferimento(punto);
    }

    public PuntoLogico Result(){
        return this.punto;
    }
}
