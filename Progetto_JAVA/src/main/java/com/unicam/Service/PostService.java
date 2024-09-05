package com.unicam.Service;

import com.unicam.Model.Evento;
import com.unicam.Model.PostTurista;
import com.unicam.Repository.PostRepository;
import com.unicam.Repository.UtenteRepository;
import org.springframework.stereotype.Service;

@Service
public class PostService {

    private final PostRepository repoPost;
    private final UtenteRepository repoUtente;

    public PostService(PostRepository repoPost,
                       UtenteRepository repoUtente){
        this.repoPost = repoPost;
        this.repoUtente = repoUtente;
    }

    public void AggiungiContenuto(PostTurista contenuto) {
        this.repoPost.save(contenuto);
    }
}
