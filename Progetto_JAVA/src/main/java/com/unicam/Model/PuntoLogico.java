package com.unicam.Model;

public class PuntoLogico {

    private String titolo;
    private String descrizione;
    private PuntoGeolocalizzato riferimento;

    public PuntoLogico(String titolo, String descrizione, PuntoGeolocalizzato punto){
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.riferimento = punto;
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

    public PuntoGeolocalizzato getRiferimento() {
        return riferimento;
    }

    public void setRiferimento(PuntoGeolocalizzato riferimento) {
        this.riferimento = riferimento;
    }
}
