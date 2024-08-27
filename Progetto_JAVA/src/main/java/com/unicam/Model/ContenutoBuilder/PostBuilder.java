package com.unicam.Model.ContenutoBuilder;

import com.unicam.Model.Contenuto;

public class PostBuilder extends Builder{

    public Contenuto BuildPost(String file){
        Post post = new Post(this.contenuto.getId(), this.contenuto.getTitolo(),
                this.contenuto.getDescrizione(), this.contenuto.getAutore(),
                this.contenuto.getStato());
        post.setFileMultimediale(file);
        return post;
    }
}
