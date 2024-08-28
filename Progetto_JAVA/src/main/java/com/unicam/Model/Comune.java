package com.unicam.Model;

public class Comune {

    private String nome;
    private PuntoMappaSemplice posizione;

    public Comune(String nome, PuntoMappaSemplice posizione){
        this.nome = nome;
        this.posizione = posizione;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PuntoMappaSemplice getPosizione() {
        return posizione;
    }

    public void setPosizione(PuntoMappaSemplice posizione) {
        this.posizione = posizione;
    }
}
