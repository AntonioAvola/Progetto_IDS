package com.unicam.dto;

import com.unicam.Model.BuilderContenuto.PostTuristaBuilder;
import com.unicam.Model.PostTurista;
import com.unicam.Model.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PostTuristaDTO {

    private String titolo;
    private String descrizione;
    private MultipartFile contenutoMultimediale;

    public PostTuristaDTO(long id, String titolo,
                                     String descrizione, MultipartFile contenutoMultimediale){
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.contenutoMultimediale = contenutoMultimediale;
    }


    public String getTitolo() {
        return titolo;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public MultipartFile getContenutoMultimediale() {
        return contenutoMultimediale;
    }

    public void setContenutoMultimediale(MultipartFile contenutoMultimediale) {
        this.contenutoMultimediale = contenutoMultimediale;
    }

    public PostTurista ToEntity(User user, String comune) throws IOException {
        PostTuristaBuilder builder = new PostTuristaBuilder();
        builder.BuildTitolo(getTitolo());
        builder.BuildDescrizione(getDescrizione());
        builder.BuildSpecifica(getContenutoMultimediale().getBytes());
        builder.BuildAutore(user);
        return builder.Result();
    }
}
