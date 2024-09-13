package com.unicam.Model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;

import java.time.Duration;
import java.time.LocalDateTime;

@Embeddable
public class Tempo {

    private LocalDateTime inizio;

    private LocalDateTime fine;

    public Tempo(LocalDateTime i, LocalDateTime f){
        this.inizio = i;
        this.fine = f;
    }

    public Tempo() { }

    public LocalDateTime getInizio() {
        return inizio;
    }

    public void setInizio(LocalDateTime inizio) {
        this.inizio = inizio;
    }

    public LocalDateTime getFine() {
        return fine;
    }

    public void setFine(LocalDateTime fine) {
        this.fine = fine;
    }

    // Calcola la durata tra inizio e fine
    public Duration getDurata() {
        return Duration.between(inizio, fine);
    }

    // Verifica se due intervalli di tempo si sovrappongono
    public boolean isOverlapping(Tempo altro) {
        return !inizio.isAfter(altro.getFine()) && !fine.isBefore(altro.getInizio());
    }

    @Override
    public String toString() {
        return "Tempo{" +
                "inizio=" + inizio +
                ", fine=" + fine +
                ", durata=" + getDurata().toHours() + " ore" +
                '}';
    }
}
