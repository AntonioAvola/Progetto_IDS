package com.unicam.dto;

import com.unicam.Model.BuilderContenuto.PostTuristaBuilder;
import com.unicam.Model.PostTurista;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PostTuristaDTO {

    private String titolo;
    private String descrizione;
    private MultipartFile data;

    public PostTuristaDTO(long id, String titolo,
                                     String descrizione, MultipartFile data){
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
    }


    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public MultipartFile getData() {
        return data;
    }

    public PostTurista ToEntity() throws IOException {
        PostTuristaBuilder builder = new PostTuristaBuilder();
        builder.BuildTitolo(getTitolo());
        builder.BuildDescrizione(getDescrizione());
        builder.BuildSpecifica(getData().getBytes());
        return builder.Result();
    }
}
