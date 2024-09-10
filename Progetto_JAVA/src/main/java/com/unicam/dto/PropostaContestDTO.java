package com.unicam.dto;

import com.unicam.Model.BuilderContenuto.ContestBuilder;
import com.unicam.Model.Contest;
import com.unicam.Model.Ruolo;
import com.unicam.Model.Tempo;
import com.unicam.Model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PropostaContestDTO {
    private String titolo;
    private String descrizione;
    private LocalDateTime inizio;
    private LocalDateTime fine;

    public PropostaContestDTO(String titolo, String descrizione,
                                         LocalDateTime inizio, LocalDateTime fine){
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.inizio = inizio;
        this.fine = fine;
    }


    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public LocalDateTime getInizio() {
        return inizio;
    }

    public LocalDateTime getFine() {
        return fine;
    }

    public Contest ToEntity(User user, String comune){
        ContestBuilder builder = new ContestBuilder();
        builder.BuildAutore(user);
        builder.BuildTitolo(getTitolo().toUpperCase(Locale.ROOT));
        builder.BuildDescrizione(getDescrizione());
        builder.BuildSpecifica(new Tempo(getInizio(), getFine()));
        builder.BuildComune(comune);
        return builder.Result();
    }
}
