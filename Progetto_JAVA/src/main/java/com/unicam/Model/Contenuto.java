package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@MappedSuperclass
public abstract class  Contenuto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String titolo;
    private String descrizione;
    //private long autoreId;
    @ManyToOne
    @JoinColumn(name = "autore_id", nullable = false)
    private User autore;
    private StatoContenuto stato;
    private String comune;
    private List<Long> idUtenteContenutoPreferito = new ArrayList<>();

    public Contenuto(){}

    public Contenuto(String titolo, String descrizione, User autore) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.autore = autore;
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

    public String getComune() {
        return comune;
    }

    public void setComune(String comune) {
        this.comune = comune;
    }

    public List<Long> getIdUtenteContenutoPreferito() {
        return idUtenteContenutoPreferito;
    }
}
