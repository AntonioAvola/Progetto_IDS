package com.unicam.dto.Risposte;

public class ComuneResponseDTO {

    private String nome;
    private LuogoDTO luogo;

    public ComuneResponseDTO(String nome){
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public LuogoDTO getLuogo() {
        return luogo;
    }

    public void setLuogo(LuogoDTO luogo) {
        this.luogo = luogo;
    }
}
