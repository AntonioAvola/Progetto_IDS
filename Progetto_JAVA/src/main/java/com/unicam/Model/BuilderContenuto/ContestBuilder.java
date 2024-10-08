package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.*;
import com.unicam.Service.UtenteService;
import com.unicam.dto.UtenteDTO;

import java.util.List;

public class ContestBuilder implements Builder{

    private Contest contest;

    public ContestBuilder(){
        this.contest = new Contest();
    }

    @Override
    public void BuildAutore(User autore) {
        this.contest.setAutore(autore);
    }

    @Override
    public void BuildTitolo(String titolo) {
        this.contest.setTitolo(titolo);
    }

    @Override
    public void BuildDescrizione(String descrizione) {
        this.contest.setDescrizione(descrizione);
    }

    @Override
    public void BuildComune(String comune) {
        this.contest.setComune(comune);
    }

    public void BuildSpecifica(Tempo tempo){
        this.contest.setDurata(tempo);
    }

    public Contest Result(){
        return this.contest;
    }
}
