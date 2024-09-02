package com.unicam.dto.Provvisori;

import com.unicam.Model.BuilderContenuto.PostTuristaBuilder;
import com.unicam.Model.PostTurista;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class PostTuristaProvvisorioDTO {

    private long idUtente;
    private String titolo;
    private String descrizione;
    private MultipartFile data;

    public PostTuristaProvvisorioDTO(long id, String titolo,
                                     String descrizione, MultipartFile data){
        this.idUtente = id;
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.data = data;
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

    public MultipartFile getData() {
        return data;
    }

    public PostTurista ToEntity() throws IOException {
        PostTuristaBuilder builder = new PostTuristaBuilder();
        builder.BuildAutore(getIdUtente());
        builder.BuildTitolo(getTitolo());
        builder.BuildDescrizione(getDescrizione());
        builder.BuildSpecifica(getData().getBytes());
        return builder.Result();
    }
}
