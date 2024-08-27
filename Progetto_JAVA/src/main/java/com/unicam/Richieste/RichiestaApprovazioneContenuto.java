package com.unicam.Richieste;

import com.unicam.Model.Contenuto;
import com.unicam.Model.User;
import com.unicam.Service.ContenutoService;

public class RichiestaApprovazioneContenuto<T extends Contenuto> implements ICommand{
    private ContenutoService<T> contenutoService;
    private User user;
    private T contenuto;

    public RichiestaApprovazioneContenuto(ContenutoService<T> contenutoService, User user, T contenuto) {
        this.contenutoService = contenutoService;
        this.user = user;
        this.contenuto = contenuto;
    }

    @Override
    public void Execute() {
        contenutoService.AggiungiContenuto(user, contenuto);
    }
}
