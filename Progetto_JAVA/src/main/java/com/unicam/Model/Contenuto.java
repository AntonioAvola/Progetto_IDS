package com.unicam.Model;

import java.time.LocalDateTime;
import java.util.List;


public class Contenuto {

    private String id;
    private String titolo;
    private String descrizione;
    private LocalDateTime dataCreazione;
    private User autore;
    private StatoContenuto stato;




    public Contenuto(String titolo, String descrizione, StatoContenuto stato) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.dataCreazione = LocalDateTime.now();
        this.stato = stato;
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

    public LocalDateTime getDataCreazione() {
        return dataCreazione;
    }

    public void setDataCreazione(LocalDateTime dataCreazione) {
        this.dataCreazione = dataCreazione;
    }

    public User getAutore() {
        return autore;
    }

    public void setAutore(User autore) {
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
                + "Data creazione: " + dataCreazione + "\n"
                + "Autore: " + autore + "\n"
                + "Stato: " + stato;
    }

}
