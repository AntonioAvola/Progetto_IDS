package com.unicam.dto.Provvisori;

import com.unicam.Model.BuilderContenuto.ItinerarioBuilder;
import com.unicam.Model.Itinerario;

import java.util.List;
import java.util.Locale;

public class ItinerarioProvvisorioDTO {

    private long idUtente;
    private String titolo;
    private String descrizione;
    private List<String> nomiPunti;

    public ItinerarioProvvisorioDTO(long id, String titolo, String descrizione, List<String> punti){
        this.idUtente = id;
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

    public long getIdUtente() {
        return idUtente;
    }

    public Itinerario ToEntity() {
        ItinerarioBuilder builder = new ItinerarioBuilder();
        builder.BuildTitolo(getTitolo().toUpperCase(Locale.ROOT));
        builder.BuildDescrizione(this.descrizione);
        return builder.Result();
    }
}
