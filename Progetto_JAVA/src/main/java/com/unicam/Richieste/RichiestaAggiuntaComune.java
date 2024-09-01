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
    private Comune comune = new Comune();

    public RichiestaAggiuntaComune(UtenteService servizio, RichiestaComuneDTO richiesta){
        this.servizioUtente = servizio;
        builderPunto.BuildAutore(servizioUtente.GetIdByUsername(richiesta.getUsername()));
        builderPunto.BuildTitolo(richiesta.getUsername().toUpperCase(Locale.ROOT));
        builderPunto.BuildDescrizione(richiesta.getDescrizione());
        builderPunto.BuildSpecifica(richiesta.getLatitudine(), richiesta.getLongitudine());
        this.punto = builderPunto.Result();
        this.punto.setStato(StatoContenuto.ATTESA);
        this.comune.setNome(richiesta.getNomeComune().toUpperCase(Locale.ROOT));
        this.comune.setPosizione(punto);
        this.comune.setStatoRichiesta(StatoContenuto.ATTESA);
    }

    @Override
    public void Execute() {
        this.servizioContenuto.AggiungiContenuto(this.punto);
        this.servizioComune.AggiungiComune(this.comune);
    }
}
