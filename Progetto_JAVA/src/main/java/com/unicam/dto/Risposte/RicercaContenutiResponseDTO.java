package com.unicam.dto.Risposte;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RicercaContenutiResponseDTO {

    private Map<String, List<?>> contenutiPresenti = new HashMap<>();

    public RicercaContenutiResponseDTO(){}

    public Map<String, List<?>> getContenutiPresenti() {
        return contenutiPresenti;
    }

    public void setContenutiPresenti(Map<String, List<?>> contenutiPresenti) {
        this.contenutiPresenti = contenutiPresenti;
    }
}
