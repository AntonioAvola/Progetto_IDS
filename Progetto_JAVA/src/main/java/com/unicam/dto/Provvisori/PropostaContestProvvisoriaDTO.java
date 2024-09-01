package com.unicam.dto.Provvisori;

import com.unicam.Model.BuilderContenuto.ContestBuilder;
import com.unicam.Model.Contest;

import java.time.LocalDateTime;
import java.util.Locale;

public class PropostaContestProvvisoriaDTO {

    private long idUtente;
    private String titolo;
    private String descrizione;
    private LocalDateTime inizio;
    private LocalDateTime fine;
    private boolean curatore;
    private boolean contributor;
    private boolean contributorAutorizzati;
    private boolean tutti;

    public PropostaContestProvvisoriaDTO(long id, String titolo, String descrizione,
                                         LocalDateTime inizio, LocalDateTime fine,
                                         boolean curatore, boolean contributor,
                                         boolean contributorAutorizzati, boolean tutti){
        this.idUtente = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.inizio = inizio;
        this.fine = fine;
        this.curatore = curatore;
        this.contributor = contributor;
        this.contributorAutorizzati = contributorAutorizzati;
        this.tutti = tutti;
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

    public boolean isCuratore() {
        return curatore;
    }

    public boolean isContributor() {
        return contributor;
    }

    public boolean isContributorAutorizzati() {
        return contributorAutorizzati;
    }

    public boolean isTutti() {
        return tutti;
    }

    public Contest ToEntity(){
        ContestBuilder builder = new ContestBuilder();
        builder.BuildAutore(getIdUtente());
        builder.BuildTitolo(getTitolo().toUpperCase(Locale.ROOT));
        builder.BuildDescrizione(getDescrizione());
        return builder.Result();
    }
}
