package com.unicam.Richieste.Contenuto;

import com.unicam.Model.Itinerario;
import com.unicam.Model.StatoContenuto;
import com.unicam.Richieste.ICommand;
import com.unicam.Service.Contenuto.ItinerarioService;

public class RichiestaAggiuntaItinerario implements ICommand {

    private ItinerarioService contenutoService;
    private Itinerario contenuto;

    public RichiestaAggiuntaItinerario(ItinerarioService contenutoService,
                                       Itinerario contenuto) {
        this.contenutoService = contenutoService;
        this.contenuto = contenuto;
        this.contenuto.setStato(StatoContenuto.ATTESA);
    }

    @Override
    public void Execute() {
        contenutoService.AggiungiContenuto(contenuto);
    }
}
