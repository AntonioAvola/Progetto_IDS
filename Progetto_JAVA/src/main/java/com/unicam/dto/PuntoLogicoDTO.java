package com.unicam.dto;

import com.unicam.Model.BuilderContenuto.Builder;
import com.unicam.Model.BuilderContenuto.PuntoLogicoBuilder;
import com.unicam.Model.PuntoLogico;

import java.util.Locale;

public class PuntoLogicoDTO {

    private String titolo;
    private String descrizione;
    private String nomePuntoGeo;

    public PuntoLogicoDTO(String titolo, String descrizione, String nomePuntoGeo){
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.nomePuntoGeo = nomePuntoGeo;
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

    public String getNomePuntoGeo() {
        return nomePuntoGeo;
    }

    public void setNomePuntoGeo(String nomePuntoGeo) {
        this.nomePuntoGeo = nomePuntoGeo;
    }

    public PuntoLogico ToEntity() {
        PuntoLogicoBuilder builder = new PuntoLogicoBuilder();
        builder.BuildTitolo(getTitolo().toUpperCase(Locale.ROOT));
        builder.BuildDescrizione(getDescrizione());
        return builder.Result();
    }
}
