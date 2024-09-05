package com.unicam.Richieste.Contenuto;

import com.unicam.Model.Contest;
import com.unicam.Model.StatoContenuto;
import com.unicam.Richieste.ICommand;
import com.unicam.Service.Contenuto.ContestService;
import com.unicam.Service.ContenutoService;

public class RichiestaAggiuntaContest implements ICommand {

    private ContestService contenutoService;
    private Contest contenuto;

    public RichiestaAggiuntaContest(ContestService contenutoService,
                                    Contest contenuto) {
        this.contenutoService = contenutoService;
        this.contenuto = contenuto;
        this.contenuto.setStato(StatoContenuto.ATTESA);
    }

    @Override
    public void Execute() {
        contenutoService.AggiungiContenuto(contenuto);
    }
}
