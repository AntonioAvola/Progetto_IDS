package com.unicam.dto;

import com.unicam.Model.BuilderContenuto.EventoBuilder;
import com.unicam.Model.Evento;
import com.unicam.Model.User;

import java.time.LocalDateTime;
import java.util.Locale;

public class PropostaEventoDTO {
    private String titolo;
    private String descrizione;
    private LocalDateTime inizio;
    private LocalDateTime fine;
    private String nomeLuogo;

    public PropostaEventoDTO( String titolo, String descrizione,
                                        LocalDateTime inizio, LocalDateTime fine,
                                        String nomeLuogo){
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.inizio = inizio;
        this.fine = fine;
        this.nomeLuogo = nomeLuogo;
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

    public String getNomeLuogo() {
        return nomeLuogo;
    }

    public Evento ToEntity(User user, String comune){
        EventoBuilder builder = new EventoBuilder();
        builder.BuildAutore(user);
        builder.BuildTitolo(getTitolo().toUpperCase(Locale.ROOT));
        builder.BuildDescrizione(getDescrizione());
        builder.BuildComune(comune);
        return builder.Result();
    }
}
