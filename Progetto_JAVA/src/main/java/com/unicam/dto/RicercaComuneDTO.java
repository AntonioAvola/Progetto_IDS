package com.unicam.dto;

import java.util.Locale;

public class RicercaComuneDTO {

    private String nome;

    public RicercaComuneDTO(String comune){
        this.nome = comune.toUpperCase(Locale.ROOT);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
