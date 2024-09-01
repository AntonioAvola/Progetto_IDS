package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.Contest;
import com.unicam.Model.Ruolo;
import com.unicam.Model.Tempo;
import com.unicam.Model.User;
import com.unicam.dto.UtenteDTO;

import java.util.List;

public class ContestBuilder implements Builder{

    private Contest contest;

    public ContestBuilder(){
        this.contest = new Contest();
    }

    @Override
    public void BuildAutore(Long autoreId) {
        this.contest.setAutoreId(autoreId);
    }

    @Override
    public void BuildTitolo(String titolo) {
        this.contest.setTitolo(titolo);
    }

    @Override
    public void BuildDescrizione(String descrizione) {
        this.contest.setDescrizione(descrizione);
    }

    public void BuildSpecifica(List<Ruolo> partecipanti, Tempo tempo){
        BuildPartecipanti(partecipanti);
        BuildDurata(tempo);
    }

    private void BuildPartecipanti(List<Ruolo> partecipanti){
        this.contest.setPartecipanti(partecipanti);
    }

    private void BuildDurata(Tempo tempo) {
        this.contest.setDurata(tempo);
    }

    public Contest Result(){
        return this.contest;
    }
}
