package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table
public class Contest extends Contenuto{

    private List<UtenteDTO> partecipanti = new ArrayList<>();

    private Tempo durata;
    private int votiFavore;
    private int votiContrari;

    public Contest(){
        super();
        this.votiFavore = 0;
        this.votiContrari = 0;
    }

    public List<UtenteDTO> getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(List<UtenteDTO> partecipanti) {
        this.partecipanti = partecipanti;
    }

    public int getVotiFavore() {
        return votiFavore;
    }

    public int getVotiContrari() {
        return votiContrari;
    }

    public Tempo getDurata() {
        return durata;
    }

    public void setDurata(Tempo durata) {
        this.durata = durata;
    }
}
