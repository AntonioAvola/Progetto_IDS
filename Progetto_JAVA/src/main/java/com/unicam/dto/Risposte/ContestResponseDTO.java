package com.unicam.dto.Risposte;

import com.unicam.Model.Tempo;

public class ContestResponseDTO {

    private String nomeItinerario;
    private String descrizione;
    private Tempo durata;
    private String autore;

    public ContestResponseDTO(String nomeItinerario, String descrizione,
                              Tempo durata, String autore){
        this.nomeItinerario = nomeItinerario;
        this.descrizione = descrizione;
        this.durata = durata;
        this.autore = autore;
    }

    public String getNomeItinerario() {
        return nomeItinerario;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Tempo getDurata() {
        return durata;
    }

    public String getAutore() {
        return autore;
    }
}
