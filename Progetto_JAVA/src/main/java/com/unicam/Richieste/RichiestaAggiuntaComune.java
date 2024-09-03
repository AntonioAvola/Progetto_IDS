package com.unicam.Richieste;

import com.unicam.Model.BuilderContenuto.PuntoGeoBuilder;
import com.unicam.Model.Comune;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.StatoContenuto;
import com.unicam.Service.ComuneService;
import com.unicam.Service.ContenutoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.RichiestaComuneDTO;

import java.util.Locale;

/**
 * Il receiver (admin in questo caso) prende dal database
 * tutti i contenuti (PuntoGeolocalizzato o Comune) e decide
 * se accettare la richiesta o no
 */
public class RichiestaAggiuntaComune implements ICommand{

    private PuntoGeoBuilder builderPunto = new PuntoGeoBuilder();
    private ContenutoService<PuntoGeolocalizzato> servizioContenuto;
    private ComuneService servizioComune;
    private UtenteService servizioUtente;
    private PuntoGeolocalizzato punto;
    private Comune comune;

    public RichiestaAggiuntaComune(UtenteService servizio,
                                   ContenutoService<PuntoGeolocalizzato> servizioPunto,
                                   ComuneService servizioComune,
                                   RichiestaComuneDTO richiesta,
                                   long id){
        this.servizioUtente = servizio;
        this.servizioContenuto = servizioPunto;
        this.servizioComune = servizioComune;
        this.punto = richiesta.ToEntityPunto(this.servizioUtente.GetUtenteById(id).getComune());
        this.punto.setAutore(this.servizioUtente.GetUtenteById(id));
        this.punto.setStato(StatoContenuto.ATTESA);
        this.comune = richiesta.ToEntityComune(this.punto.getAutore().getComune());
        this.comune.setPosizione(punto);
        this.comune.setStatoRichiesta(StatoContenuto.ATTESA);
    }

    @Override
    public void Execute() {
        this.servizioContenuto.AggiungiContenuto(this.punto);
        this.servizioComune.AggiungiComune(this.comune);
    }
}
