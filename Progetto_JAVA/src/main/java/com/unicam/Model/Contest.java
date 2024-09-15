package com.unicam.Model;

import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contest")

public class Contest extends Contenuto{

    @Embedded
    private Tempo durata;
    private String vincitore;

    private List<String> listaPartecipanti;

    public Contest(){
        super();
        this.vincitore = "";
        this.listaPartecipanti = new ArrayList<>();
    }

    public Tempo getDurata() {
        return durata;
    }

    public void setDurata(Tempo durata) {
        this.durata = durata;
    }

    public List<String> getListaPartecipanti() {
        return listaPartecipanti;
    }

    public String getVincitore() {
        return vincitore;
    }

    public void setVincitore(String vincitore) {
        this.vincitore = vincitore;
    }
}
