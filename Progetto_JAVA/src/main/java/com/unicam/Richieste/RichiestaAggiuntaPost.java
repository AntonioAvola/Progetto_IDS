package com.unicam.Richieste;

import com.unicam.Model.BuilderContenuto.PostTuristaBuilder;
import com.unicam.Model.PostTurista;
import com.unicam.Model.StatoContenuto;
import com.unicam.Service.ContenutoService;

import javax.swing.*;

public class RichiestaAggiuntaPost implements ICommand{

    private PostTurista post;
    private ContenutoService<PostTurista> servizio;

    public RichiestaAggiuntaPost(ContenutoService<PostTurista> servizioPost,
                                 PostTurista post){
        this.servizio = servizioPost;
        PostTuristaBuilder builder = new PostTuristaBuilder();
        builder.BuildAutore(post.getAutoreId());
        builder.BuildTitolo(post.getTitolo());
        builder.BuildDescrizione(post.getDescrizione());
        builder.BuildSpecifica(post.getFileData());
        this.post = builder.Result();
        this.post.setStato(StatoContenuto.ATTESA);
    }
    @Override
    public void Execute() {
        this.servizio.AggiungiContenuto(this.post);
    }
}
