package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.Contest;
import com.unicam.Model.Tempo;
import com.unicam.dto.UtenteDTO;

import java.util.List;

public class ContestBuilder implements Builder{

    private Contest contest;

    public ContestBuilder(){
        this.contest = new Contest();
    }

    @Override
    public void BuildAutore(UtenteDTO autore) {
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

    public void BuildSpecifica(List<UtenteDTO> partecipanti, Tempo tempo){
        BuildPartecipanti(partecipanti);
        BuildDurata(tempo);
    }

    private void BuildPartecipanti(List<UtenteDTO> partecipanti){
        this.contest.setPartecipanti(partecipanti);
    }

    private void BuildDurata(Tempo tempo) {
        this.contest.setDurata(tempo);
    }

    public Contest Result(){
        return this.contest;
    }
}
