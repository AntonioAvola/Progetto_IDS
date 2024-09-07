package com.unicam.Richieste;

import com.unicam.Model.BuilderContenuto.PuntoGeoBuilder;
import com.unicam.Model.Comune;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.StatoContenuto;
import com.unicam.Service.ComuneService;
import com.unicam.Service.Contenuto.PuntoGeoService;
import com.unicam.Service.UtenteService;
import com.unicam.dto.RichiestaComuneDTO;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Locale;

/**
 * Il receiver (admin in questo caso) prende dal database
 * tutti i contenuti (PuntoGeolocalizzato o Comune) e decide
 * se accettare la richiesta o no
 */
public class RichiestaAggiuntaComune implements ICommand{

    private PuntoGeoBuilder builderPunto = new PuntoGeoBuilder();
    //private ContenutoService<PuntoGeolocalizzato> servizioContenuto;
    private PuntoGeoService servizioContenuto;
    private ComuneService servizioComune;
    private UtenteService servizioUtente;
    private PuntoGeoService servizioPuntoGeo;
    private PuntoGeolocalizzato punto;
    private Comune comune;

    public RichiestaAggiuntaComune(UtenteService servizio,
                                   PuntoGeoService servizioPunto,
                                   ComuneService servizioComune,
                                   RichiestaComuneDTO richiesta,
                                   PuntoGeoService servizioPuntoGeo,
                                   long id){
        this.servizioUtente = servizio;
        this.servizioContenuto = servizioPunto;
        this.servizioComune = servizioComune;
        this.servizioPuntoGeo = servizioPuntoGeo;
        this.punto = richiesta.ToEntityPunto(this.servizioUtente.GetUtenteById(id).getComune());
        this.punto.setAutore(this.servizioUtente.GetUtenteById(id));
        this.punto.setStato(StatoContenuto.ATTESA);
        this.comune = richiesta.ToEntityComune(this.punto.getAutore().getComune());
        this.comune.setPosizione(punto);
        this.comune.setStatoRichiesta(StatoContenuto.ATTESA);
    }

    @Override
    public void Execute() {
        this.servizioPuntoGeo.ContienePunto(punto);
        this.servizioContenuto.AggiungiPunto(this.punto);
        this.servizioComune.AggiungiComune(this.comune);
    }
}
