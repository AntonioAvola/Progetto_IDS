package com.unicam.Richieste.Contenuto;

import com.unicam.Model.Evento;
import com.unicam.Model.StatoContenuto;
import com.unicam.Richieste.ICommand;
import com.unicam.Service.Contenuto.EventoService;
import com.unicam.Service.ContenutoService;

public class RichiestaAggiuntaEvento implements ICommand {

    private EventoService contenutoService;
    private Evento contenuto;

    public RichiestaAggiuntaEvento(EventoService contenutoService,
                                   Evento contenuto) {
        this.contenutoService = contenutoService;
        this.contenuto = contenuto;
        this.contenuto.setStato(StatoContenuto.ATTESA);
    }

    @Override
    public void Execute() {
        contenutoService.AggiungiContenuto(contenuto);
    }
}
