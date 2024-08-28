package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.*;
import com.unicam.dto.UtenteDTO;

public class EventoBuilder implements Builder{

    private Evento evento;

    public EventoBuilder(){
        this.evento = new Evento();
    }

    @Override
    public void BuildAutore(UtenteDTO autore) {
        this.evento.setAutore(autore);
    }

    @Override
    public void BuildTitolo(String titolo) {
        this.evento.setTitolo(titolo);
    }

    @Override
    public void BuildDescrizione(String descrizione) {
        this.evento.setDescrizione(descrizione);
    }

    public void BuildSpecifica(Tempo tempo, PuntoMappaSemplice punto){
        BuildInizioFine(tempo);
        BuildLuogo(punto);
    }

    private void BuildLuogo(PuntoMappaSemplice punto) {
        this.evento.setLuogo(punto);
    }

    private void BuildInizioFine(Tempo tempo) {
        this.evento.setDurata(tempo);
    }

    public Evento Result(){
        return this.evento;
    }
}
