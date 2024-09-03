package com.unicam.dto.Provvisori;

import com.unicam.Model.BuilderContenuto.EventoBuilder;
import com.unicam.Model.Evento;

import java.time.LocalDateTime;
import java.util.Locale;

public class PropostaEventoProvvisoriaDTO {

    private long idUtente;
    private String titolo;
    private String descrizione;
    private LocalDateTime inizio;
    private LocalDateTime fine;
    private String nomeLuogo;

    public PropostaEventoProvvisoriaDTO(long id, String titolo, String descrizione,
                                        LocalDateTime inizio, LocalDateTime fine,
                                        String nomeLuogo){
        this.idUtente = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.inizio = inizio;
        this.fine = fine;
        this.nomeLuogo = nomeLuogo;
    }

    public long getIdUtente() {
        return idUtente;
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

    public Evento ToEntity(){
        EventoBuilder builder = new EventoBuilder();
        builder.BuildTitolo(getTitolo().toUpperCase(Locale.ROOT));
        builder.BuildDescrizione(getDescrizione());
        return builder.Result();
    }
}
