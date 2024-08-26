package com.unicam.Model;

public class Post extends Contenuto{

    private String fileMultimediale;

    public Post(Long id, String titolo, String descrizione, User autore, StatoContenuto stato) {
        super(id, titolo, descrizione, autore, stato);
    }

    public String getFileMultimediale() {
        return fileMultimediale;
    }

    public void setFileMultimediale(String fileMultimediale) {
        this.fileMultimediale = fileMultimediale;
    }
}
