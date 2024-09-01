package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class  Contenuto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String titolo;
    private String descrizione;
    private long autoreId;
    private StatoContenuto stato;

    public Contenuto(){}

    public Contenuto(String titolo, String descrizione, Long autore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.autoreId = autore;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public long getAutoreId() {
        return autoreId;
    }

    public void setAutoreId(long autoreId) {
        this.autoreId = autoreId;
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
                + "Autore: " + autoreId + "\n"
                + "Stato: " + stato;
    }


}
