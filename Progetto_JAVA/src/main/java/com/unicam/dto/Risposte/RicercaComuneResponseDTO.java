package com.unicam.dto.Risposte;

import com.unicam.dto.RicercaComuneDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RicercaComuneResponseDTO {

    private Map<String, List<?>> contenutiComune = new HashMap<>();

    public RicercaComuneResponseDTO(){}

    public Map<String, List<?>> getContenutiComune() {
        return contenutiComune;
    }

    public void setContenutiComune(Map<String, List<?>> contenutiComune) {
        this.contenutiComune = contenutiComune;
    }
}
