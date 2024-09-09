package com.unicam.dto;

import java.util.Locale;

public class AccettaRifiutaPuntoLogicoDTO {

    private String titoloAvviso;
    private String nomeLuogo;

    public AccettaRifiutaPuntoLogicoDTO(String titoloAvviso, String nomeLuogo){
        this.titoloAvviso = titoloAvviso.toUpperCase(Locale.ROOT);
        this.nomeLuogo = nomeLuogo.toUpperCase(Locale.ROOT);
    }

    public String getTitoloAvviso() {
        return titoloAvviso;
    }

    public String getNomeLuogo() {
        return nomeLuogo;
    }
}
