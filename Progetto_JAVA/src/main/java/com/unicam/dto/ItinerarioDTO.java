package com.unicam.dto;

import com.unicam.Model.BuilderContenuto.Builder;
import com.unicam.Model.BuilderContenuto.ItinerarioBuilder;
import com.unicam.Model.Itinerario;
import org.hibernate.dialect.function.IntegralTimestampaddFunction;

import java.util.List;

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

    public Itinerario ToEntity() {
        ItinerarioBuilder builder = new ItinerarioBuilder();
        builder.BuildTitolo(this.titolo);
        builder.BuildDescrizione(this.descrizione);
        return builder.Result();
    }
}
