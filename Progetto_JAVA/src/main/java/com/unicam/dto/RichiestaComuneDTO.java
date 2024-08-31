package com.unicam.dto;

public class RichiestaComuneDTO {

    private String nomeComune;
    private String descrizione;
    private String username;
    private double latitudine;
    private double longitudine;


    public RichiestaComuneDTO(){}
    public RichiestaComuneDTO(String nome, String descrizione, String username, double lat, double lon){
        this.nomeComune = nome;
        this.descrizione = descrizione;
        this.username = username;
        this.latitudine = lat;
        this.longitudine = lon;
    }

    public String getNomeComune() {
        return nomeComune;
    }

    public void setNomeComune(String nomeComune) {
        this.nomeComune = nomeComune;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(double latitudine) {
        this.latitudine = latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(double longitudine) {
        this.longitudine = longitudine;
    }
}
