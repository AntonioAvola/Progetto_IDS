package com.unicam.Model.ContenutoBuilder;

import com.unicam.Model.Contenuto;
import com.unicam.Model.Itinerario;
import com.unicam.Model.PuntoGeolocalizzato;

import java.util.List;

public class ItinerarioBuilder extends Builder{

    public Contenuto BuildItinerario(List<PuntoGeolocalizzato> interessi){
        Itinerario itineraio = new Itinerario(this.contenuto.getId(), this.contenuto.getTitolo(),
                this.contenuto.getDescrizione(), this.contenuto.getAutore(),
                this.contenuto.getStato());
        itineraio.setPuntiDiInteresse(interessi);
        return itineraio;
    }
}
