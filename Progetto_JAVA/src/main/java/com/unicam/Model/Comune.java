package com.unicam.Model;

public class Comune {

    private String nome;
    private PuntoGeolocalizzato posizione;

    public Comune(String nome, PuntoGeolocalizzato posizione){
        this.nome = nome;
        this.posizione = posizione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PuntoGeolocalizzato getPosizione() {
        return posizione;
    }

    public void setPosizione(PuntoGeolocalizzato posizione) {
        this.posizione = posizione;
    }
}
