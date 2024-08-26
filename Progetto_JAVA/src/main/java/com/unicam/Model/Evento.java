package com.unicam.Model;

import java.time.LocalDateTime;

public class Evento {

    private User creatore;
    private LocalDateTime dataInizio;
    private LocalDateTime dataFine;

    private String descrizione;
    private PuntoGeolocalizzato luogo;

    public Evento(User user, LocalDateTime inizio, LocalDateTime fine, String descrizione, PuntoGeolocalizzato punto){
        this.creatore = user;
        this.dataInizio = inizio;
        this.dataFine = fine;
        this.descrizione = descrizione;
        this.luogo = punto;
    }

    public User getCreatore() {
        return creatore;
    }

    public void setCreatore(User creatore) {
        this.creatore = creatore;
    }

    public LocalDateTime getDataInizio() {
        return dataInizio;
    }

    public void setDataInizio(LocalDateTime dataInizio) {
        this.dataInizio = dataInizio;
    }

    public LocalDateTime getDataFine() {
        return dataFine;
    }

    public void setDataFine(LocalDateTime dataFine) {
        this.dataFine = dataFine;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public PuntoGeolocalizzato getLuogo() {
        return luogo;
    }

    public void setLuogo(PuntoGeolocalizzato luogo) {
        this.luogo = luogo;
    }

    //aggiungere lo stato (pending o no)?
}
