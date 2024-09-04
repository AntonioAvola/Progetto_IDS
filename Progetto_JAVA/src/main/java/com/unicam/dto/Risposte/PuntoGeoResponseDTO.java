package com.unicam.dto.Risposte;

public class PuntoGeoResponseDTO {
    private String nome;
    private String descrizione;
    private double latitudine;
    private double longitudine;
    private String autore;

    public PuntoGeoResponseDTO(String nome, String descrizione,
                               double latitudine, double longitudine,
                               String autore){
        this.nome = nome;
        this.descrizione = descrizione;
        this.latitudine = latitudine;
        this.longitudine = longitudine;
        this.autore = autore;
    }

    public String getNome() {
        return nome;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public double getLongitudine() {
        return longitudine;
    }

    public String getAutore() {
        return autore;
    }
}
