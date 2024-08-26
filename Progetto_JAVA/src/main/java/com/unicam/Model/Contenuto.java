package com.unicam.Model;

import java.time.LocalDateTime;

public class Contenuto {

    private Long id;
    private String titolo;
    private String descrizione;
    private TipoContenuto tipo;
    private LocalDateTime dataCreazione;
    private User autore;
    private PuntoGeolocalizzato puntoDiInteresse;
    private StatoContenuto stato;


    public Contenuto(Long id, String titolo, String descrizione, TipoContenuto tipo, User autore,
                     PuntoGeolocalizzato puntoDiInteresse, StatoContenuto stato) {
        this.id = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.tipo = tipo;
        this.dataCreazione = LocalDateTime.now();
        this.autore = autore;
        this.puntoDiInteresse = puntoDiInteresse;
        this.stato = stato;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public TipoContenuto getTipo() {
        return tipo;
    }

    public void setTipo(TipoContenuto tipo) {
        this.tipo = tipo;
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

    public PuntoGeolocalizzato getPuntoDiInteresse() {
        return puntoDiInteresse;
    }

    public void setPuntoDiInteresse(PuntoGeolocalizzato puntoDiInteresse) {
        this.puntoDiInteresse = puntoDiInteresse;
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
                + "Tipo: " + tipo + "\n"
                + "Data creazione: " + dataCreazione + "\n"
                + "Autore: " + autore + "\n"
                + "Punto di interesse: " + puntoDiInteresse + "\n"
                + "Stato: " + stato;
    }

}
