package com.unicam.Richieste;

import com.unicam.Model.BuilderContenuto.PostTuristaBuilder;
import com.unicam.Model.PostTurista;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import com.unicam.Repository.PostRepository;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.PostService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.PostTuristaDTO;
import com.unicam.dto.Provvisori.PostTuristaProvvisorioDTO;

import javax.swing.*;
import java.io.IOException;

public class RichiestaAggiuntaPost implements ICommand{

    private PostTurista post;
    //private ContenutoService<PostTurista> servizio;
    private PostService servizio;
    private UtenteService servizioUTente;

    public RichiestaAggiuntaPost(PostService servizioPost,
                                 UtenteService servizio,
                                 PostTuristaDTO post, User user, String comune) throws IOException {
        this.servizio = servizioPost;
        this.servizioUTente = servizio;
        this.post = post.ToEntity(user,comune);
        this.post.setStato(StatoContenuto.ATTESA);
    }
    @Override
    public void Execute() {
        this.servizio.AggiungiContenuto(this.post);
    }
}
