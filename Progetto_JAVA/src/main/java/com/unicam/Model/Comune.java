package com.unicam.Model;

import jakarta.persistence.*;

@Entity
@Table
public class Comune {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nome;

    @OneToOne
    private PuntoGeolocalizzato posizione;
    private StatoContenuto statoRichiesta;

    public Comune(String nome, PuntoGeolocalizzato posizione){
        this.nome = nome;
        this.posizione = posizione;
    }

    //Costruttore adibito a springboot per istanziare l'entità
    public Comune(){}

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public PuntoGeolocalizzato getPosizione() {
        return posizione;
    }

    public void setPosizione(PuntoGeolocalizzato posizione) {
        this.posizione = posizione;
    }

    public StatoContenuto getStatoRichiesta() {
        return statoRichiesta;
    }

    public void setStatoRichiesta(StatoContenuto statoRichiesta) {
        this.statoRichiesta = statoRichiesta;
    }
}
