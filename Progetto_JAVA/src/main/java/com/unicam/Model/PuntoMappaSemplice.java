package com.unicam.Model;

import java.util.List;

public class PuntoMappaSemplice {

    private double latitudine;
    private double longitudinale;

    public  PuntoMappaSemplice(){}
    public PuntoMappaSemplice(double latitudine, double longitudinale){
        this.longitudinale = longitudinale;
        this.latitudine = latitudine;
    }

    public double getLatitudine() {
        return latitudine;
    }

    public double getLongitudinale() {
        return longitudinale;
    }
}
