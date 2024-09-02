package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.PostTurista;

public class PostTuristaBuilder implements Builder{

    private PostTurista post;

    public PostTuristaBuilder(){
        this.post = new PostTurista();
    }


    @Override
    public void BuildAutore(Long autoreId) {
        this.post.setAutoreId(autoreId);
    }

    @Override
    public void BuildTitolo(String titolo) {
        this.post.setTitolo(titolo);
    }

    @Override
    public void BuildDescrizione(String descrizione) {
        this.post.setDescrizione(descrizione);
    }

    public void BuildSpecifica(byte[] data){
        this.post.setFileData(data);
    }

    public PostTurista Result(){
        return this.post;
    }
}
