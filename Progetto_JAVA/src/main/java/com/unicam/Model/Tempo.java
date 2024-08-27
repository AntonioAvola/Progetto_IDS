package com.unicam.Model;

import java.time.LocalDateTime;

public class Tempo {

    private LocalDateTime inizio;

    private LocalDateTime fine;

    public Tempo(LocalDateTime i, LocalDateTime f){
        this.inizio = i;
        this.fine = f;
    }

    public LocalDateTime getInizio() {
        return inizio;
    }

    public LocalDateTime getFine() {
        return fine;
    }

}
