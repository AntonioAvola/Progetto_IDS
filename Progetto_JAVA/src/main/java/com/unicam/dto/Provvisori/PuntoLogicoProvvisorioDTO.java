package com.unicam.dto.Provvisori;

import com.unicam.Model.BuilderContenuto.PuntoLogicoBuilder;
import com.unicam.Model.PuntoLogico;

import java.util.Locale;

public class PuntoLogicoProvvisorioDTO {

    private long idUtente;
    private String titolo;
    private String descrizione;
    private String nomePuntoGeo;

    public PuntoLogicoProvvisorioDTO(long idUtente, String titolo, String descrizione, String nomePuntoGeo){
        this.idUtente = idUtente;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.nomePuntoGeo = nomePuntoGeo.toUpperCase(Locale.ROOT);
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

    public long getIdUtente() {
        return idUtente;
    }

    public PuntoLogico ToEntity() {
        PuntoLogicoBuilder builder = new PuntoLogicoBuilder();
        builder.BuildTitolo(getTitolo().toUpperCase(Locale.ROOT));
        builder.BuildDescrizione(getDescrizione());
        return builder.Result();
    }
}
