package com.unicam.dto.Risposte;

import com.unicam.Model.Itinerario;

import java.util.List;

public class ItinerarioResponseDTO {

    private String nomeItinerario;
    private String descrizione;
    private List<LuogoDTO> luoghi;
    private String autore;

    public ItinerarioResponseDTO(String nomeItinerario, String descrizione,
                                 String autore){
        this.nomeItinerario = nomeItinerario;
        this.descrizione = descrizione;
        this.autore = autore;
    }

    public String getNomeItinerario() {
        return nomeItinerario;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public List<LuogoDTO> getLuogo() {
        return luoghi;
    }

    public void setLuoghi(List<LuogoDTO> luoghi){
        this.luoghi = luoghi;
    }

    public String getAutore() {
        return autore;
    }
}
