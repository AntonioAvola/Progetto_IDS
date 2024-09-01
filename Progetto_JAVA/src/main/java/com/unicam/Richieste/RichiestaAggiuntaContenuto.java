package com.unicam.Richieste;

import com.unicam.Model.Contenuto;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import com.unicam.Service.ContenutoService;

public class RichiestaAggiuntaContenuto<T extends Contenuto> implements ICommand{
    private ContenutoService<T> contenutoService;
    private User user;
    private T contenuto;

    public RichiestaAggiuntaContenuto(ContenutoService<T> contenutoService, T contenuto) {
        this.contenutoService = contenutoService;
        this.user = user;
        this.contenuto = contenuto;
        this.contenuto.setStato(StatoContenuto.ATTESA);
    }

    @Override
    public void Execute() {
        contenutoService.AggiungiContenuto(contenuto);
    }
}
