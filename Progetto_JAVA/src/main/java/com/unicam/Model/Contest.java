package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "contest")

public class Contest extends Contenuto{

    /**
     * lista di ruoli, in cui vengono specificati quali utenti possono
     * partecipare in base al ruolo che hanno.
     * Esempio:
     * un contest potrebbe essere reso disponibile solo per contributor e contributors
     * autorizzati, quindi si associano i due ruoli al contest e chiunque ha quel ruolo
     * può vedere sulla pagina del comune quali contest sono inseriti e partecipare
     */
    private List<Ruolo> partecipanti = new ArrayList<>();
    @Embedded
    private Tempo durata;
    private int votiFavore;
    private int votiContrari;

    private List<Long> idPartecipanti;

    public Contest(){
        super();
        this.votiFavore = 0;
        this.votiContrari = 0;
        this.idPartecipanti = new ArrayList<>();
    }


    public List<Ruolo> getPartecipanti() {
        return partecipanti;
    }

    public void setPartecipanti(List<Ruolo> partecipanti) {
        this.partecipanti = partecipanti;
    }

    public Tempo getDurata() {
        return durata;
    }

    public void setDurata(Tempo durata) {
        this.durata = durata;
    }

    public List<Long> getIdPartecipanti() {
        return idPartecipanti;
    }

    public void setIdPartecipanti(List<Long> idPartecipanti) {
        this.idPartecipanti = idPartecipanti;
    }

    public int getVotiFavore() {
        return votiFavore;
    }

    public void setVotiFavore(int votiFavore) {
        this.votiFavore = votiFavore;
    }

    public int getVotiContrari() {
        return votiContrari;
    }

    public void setVotiContrari(int votiContrari) {
        this.votiContrari = votiContrari;
    }
}
