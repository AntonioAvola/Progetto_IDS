package com.unicam.Model;

import com.unicam.dto.UtenteDTO;

import java.time.LocalDateTime;
import java.util.List;


public abstract class  Contenuto {

    private String id;
    private String titolo;
    private String descrizione;
    private UtenteDTO autore;
    private StatoContenuto stato;

    public Contenuto(){}

    public Contenuto(String titolo, String descrizione, UtenteDTO autore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.autore = autore;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public UtenteDTO getAutore() {
        return autore;
    }

    public void setAutore(UtenteDTO autore) {
        this.autore = autore;
    }

    public StatoContenuto getStato() {
        return stato;
    }

    public void setStato(StatoContenuto stato) {
        this.stato = stato;
    }

    @Override
    public String toString(){
        return "Titolo: " + titolo + "\n"
                + "Descrizione: "+ descrizione + "\n"
                + "Autore: " + autore + "\n"
                + "Stato: " + stato;
    }

}
