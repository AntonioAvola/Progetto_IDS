package com.unicam.Richieste;

import com.unicam.Model.Contenuto;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;

public class RichiestaAggiuntaContenuto<T extends Contenuto> implements ICommand{
    private ContenutoService<T> contenutoService;
    private UtenteService servizioUtente;
    private User user;
    private T contenuto;

    public RichiestaAggiuntaContenuto(ContenutoService<T> contenutoService,
                                      UtenteService servizioUtente,
                                      T contenuto, long id) {
        this.contenutoService = contenutoService;
        this.servizioUtente = servizioUtente;
        this.contenuto = contenuto;
        this.contenuto.setStato(StatoContenuto.ATTESA);
        this.contenuto.setAutore(this.servizioUtente.GetUtenteById(id));
    }

    @Override
    public void Execute() {
        contenutoService.AggiungiContenuto(contenuto);
    }
}
