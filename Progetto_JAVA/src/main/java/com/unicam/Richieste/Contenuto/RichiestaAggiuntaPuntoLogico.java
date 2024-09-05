package com.unicam.Richieste.Contenuto;

import com.unicam.Model.PuntoLogico;
import com.unicam.Model.StatoContenuto;
import com.unicam.Richieste.ICommand;
import com.unicam.Service.Contenuto.PuntoLogicoService;
import com.unicam.Service.ContenutoService;

public class RichiestaAggiuntaPuntoLogico implements ICommand {

    private PuntoLogicoService contenutoService;
    private PuntoLogico contenuto;

    public RichiestaAggiuntaPuntoLogico(PuntoLogicoService contenutoService,
                                        PuntoLogico contenuto) {
        this.contenutoService = contenutoService;
        this.contenuto = contenuto;
        this.contenuto.setStato(StatoContenuto.ATTESA);
    }

    @Override
    public void Execute() {
        contenutoService.AggiungiContenuto(contenuto);
    }
}
