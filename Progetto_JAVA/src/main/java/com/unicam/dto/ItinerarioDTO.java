package com.unicam.dto;

import com.unicam.Model.BuilderContenuto.ItinerarioBuilder;
import com.unicam.Model.Itinerario;
import com.unicam.Model.User;

import java.util.List;
import java.util.Locale;

public class ItinerarioDTO {

    private String titolo;
    private String descrizione;
    private List<String> nomiPunti;

    public ItinerarioDTO(String titolo, String descrizione, List<String> punti){
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.nomiPunti = punti;
    }

    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public List<String> getNomiPunti() {
        return nomiPunti;
    }

    public void setNomiPunti(List<String> nomiPunti) {
        this.nomiPunti = nomiPunti;
    }

    public Itinerario ToEntity(User user) {
        ItinerarioBuilder builder = new ItinerarioBuilder();
        builder.BuildAutore(user);
        builder.BuildTitolo(getTitolo().toUpperCase(Locale.ROOT));
        builder.BuildDescrizione(getDescrizione());
        return builder.Result();
    }
}
