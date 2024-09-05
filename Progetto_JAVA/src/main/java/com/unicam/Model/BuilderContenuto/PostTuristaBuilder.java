package com.unicam.Model.BuilderContenuto;

import com.unicam.Model.Comune;
import com.unicam.Model.PostTurista;
import com.unicam.Model.User;

public class PostTuristaBuilder implements Builder{

    private PostTurista post;

    public PostTuristaBuilder(){
        this.post = new PostTurista();
    }


    @Override
    public void BuildAutore(User autoreId) {
        this.post.setAutore(autoreId);
    }

    @Override
    public void BuildTitolo(String titolo) {
        this.post.setTitolo(titolo);
    }

    @Override
    public void BuildDescrizione(String descrizione) {
        this.post.setDescrizione(descrizione);
    }

    @Override
    public void BuildComune(String comune) {
        this.post.setComune(comune);
    }

    public void BuildSpecifica(byte[] data){
        this.post.setContenutoMultimediale(data);
    }

    public PostTurista Result(){
        return this.post;
    }
}
