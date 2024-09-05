package com.unicam.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "postTurista")
public class PostTurista extends Contenuto{

    @Lob
    @Column(name = "file_data")
    private byte[] contenutoMultimediale; // per memorizzare il file multimediale

    public byte[] getContenutoMultimediale() {
        return contenutoMultimediale;
    }

    public void setContenutoMultimediale(byte[] contenutoMultimediale) {
        this.contenutoMultimediale = contenutoMultimediale;
    }
}
