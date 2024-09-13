package com.unicam.Model;

import com.unicam.dto.UtenteDTO;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "evento")
public class Evento extends Contenuto{

    @Embedded
    private Tempo durata;

    @ManyToOne
    @JoinColumn(name = "posizione_id")
    private PuntoGeolocalizzato luogo;

    public Evento(){
        super();
    }

    public PuntoGeolocalizzato getLuogo() {
        return luogo;
    }

    public void setLuogo(PuntoGeolocalizzato luogo) {
        this.luogo = luogo;
    }


    public Tempo getDurata() {
        return durata;
    }

    public void setDurata(Tempo durata){
        this.durata = durata;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Evento evento = (Evento) o;
        return Objects.equals(durata, evento.durata) &&
                Objects.equals(luogo, evento.luogo) &&
                Objects.equals(getTitolo(), evento.getTitolo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(durata, luogo, getTitolo());
    }
}
