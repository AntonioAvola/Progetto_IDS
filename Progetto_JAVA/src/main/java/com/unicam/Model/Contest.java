package com.unicam.Model;

import com.unicam.dto.UtenteDTO;

import java.util.ArrayList;
import java.util.List;

public class Contest extends Contenuto{

    private List<UtenteDTO> partecipanti = new ArrayList<>();

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
}
