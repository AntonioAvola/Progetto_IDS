package com.unicam.Richieste.Contenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.StatoContenuto;
import com.unicam.Richieste.ICommand;
import com.unicam.Service.Contenuto.PuntoGeoService;

public class RichiestaAggiuntaPuntoGeo implements ICommand {

    private PuntoGeoService contenutoService;
    private PuntoGeolocalizzato contenuto;

    public RichiestaAggiuntaPuntoGeo(PuntoGeoService contenutoService,
                                     PuntoGeolocalizzato contenuto) {
        this.contenutoService = contenutoService;
        this.contenuto = contenuto;
        this.contenuto.setStato(StatoContenuto.ATTESA);
    }

    @Override
    public void Execute() {
        contenutoService.AggiungiContenuto(contenuto);
    }
}
