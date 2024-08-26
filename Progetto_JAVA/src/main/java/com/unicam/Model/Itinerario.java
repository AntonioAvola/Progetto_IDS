package com.unicam.Model;

import java.util.List;
import java.util.ArrayList;


public class Itinerario {
    private int id;
    private String nome;
    private String descrizione;
    private List<PuntoGeolocalizzato> puntiDiInteresse = new ArrayList<PuntoGeolocalizzato>();

    public Itinerario(int id, String nome, String descrizione) {
        this.id = id;
        this.nome = nome;
        this.descrizione = descrizione;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<PuntoGeolocalizzato> getPuntiDiInteresse() {
        return puntiDiInteresse;
    }

    public void setPuntiDiInteresse(List<PuntoGeolocalizzato> puntiDiInteresse) {
        this.puntiDiInteresse = puntiDiInteresse;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }
}
