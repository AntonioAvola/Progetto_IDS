package com.unicam.dto.Risposte;

public class PuntoLogicoResponseDTO {

    private String nomeAvviso;
    private String descrizione;
    private LuogoDTO luogo;
    private String autore;

    public PuntoLogicoResponseDTO(String nomeAvviso, String descrizione, String autore){
        this.nomeAvviso = nomeAvviso;
        this.descrizione = descrizione;
        this.autore = autore;
    }

    public String getNomeAvviso() {
        return nomeAvviso;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LuogoDTO getLuogo() {
        return luogo;
    }

    public void setLuogo(LuogoDTO luogo){
        this.luogo = luogo;
    }

    public String getAutore() {
        return autore;
    }
}
