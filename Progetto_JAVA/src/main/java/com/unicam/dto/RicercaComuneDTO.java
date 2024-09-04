package com.unicam.dto;

public class RicercaComuneDTO {

    private String nome;

    public RicercaComuneDTO(String comune){
        this.nome = comune;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
