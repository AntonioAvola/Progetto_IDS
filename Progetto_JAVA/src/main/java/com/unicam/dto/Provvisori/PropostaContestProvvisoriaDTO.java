package com.unicam.dto.Provvisori;

import com.unicam.Model.BuilderContenuto.ContestBuilder;
import com.unicam.Model.Contest;
import com.unicam.Model.Ruolo;
import com.unicam.Model.Tempo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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
    private boolean turistaAutenticato;

    public PropostaContestProvvisoriaDTO(long id, String titolo, String descrizione,
                                         LocalDateTime inizio, LocalDateTime fine,
                                         boolean curatore, boolean contributor,
                                         boolean contributorAutorizzati, boolean turistaAutenticato){
        this.idUtente = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.inizio = inizio;
        this.fine = fine;
        this.curatore = curatore;
        this.contributor = contributor;
        this.contributorAutorizzati = contributorAutorizzati;
        this.turistaAutenticato = turistaAutenticato;
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

    public boolean isTuristaAutenticato() {
        return turistaAutenticato;
    }

    public Contest ToEntity(){
        ContestBuilder builder = new ContestBuilder();
        builder.BuildTitolo(getTitolo().toUpperCase(Locale.ROOT));
        builder.BuildDescrizione(getDescrizione());
        builder.BuildSpecifica(RuoliPartecipanti(), new Tempo(getInizio(), getFine()));
        return builder.Result();
    }

    private List<Ruolo> RuoliPartecipanti() {
        List<Ruolo> ruoli = new ArrayList<>();
        if(isCuratore())
            ruoli.add(Ruolo.CURATORE);
        if(isContributor())
            ruoli.add(Ruolo.CONTRIBUTOR);
        if(isContributorAutorizzati())
            ruoli.add(Ruolo.CONTRIBUTOR_AUTORIZZATO);
        if(isTuristaAutenticato())
            ruoli.add(Ruolo.TURISTA_AUTENTICATO);
        return ruoli;
    }
}
