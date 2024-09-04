package com.unicam.Richieste;

import com.unicam.Model.BuilderContenuto.PostTuristaBuilder;
import com.unicam.Model.PostTurista;
import com.unicam.Model.StatoContenuto;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.PostTuristaDTO;
import com.unicam.dto.Provvisori.PostTuristaProvvisorioDTO;

import javax.swing.*;
import java.io.IOException;

public class RichiestaAggiuntaPost implements ICommand{

    private PostTurista post;
    private ContenutoService<PostTurista> servizio;
    private UtenteService servizioUTente;

    public RichiestaAggiuntaPost(ContenutoService<PostTurista> servizioPost,
                                 UtenteService servizio,
                                 PostTuristaDTO post) throws IOException {
        this.servizio = servizioPost;
        this.servizioUTente = servizio;
        this.post = post.ToEntity();

        /*PostTuristaBuilder builder = new PostTuristaBuilder();
        builder.BuildAutore(this.servizioUTente.GetUtenteById(idUtente));
        builder.BuildTitolo(post.getTitolo());
        builder.BuildDescrizione(post.getDescrizione());
        builder.BuildSpecifica(post.getFileData());
        this.post = builder.Result();*/
        this.post.setStato(StatoContenuto.ATTESA);
    }
    @Override
    public void Execute() {
        this.servizio.AggiungiContenuto(this.post);
    }
}
