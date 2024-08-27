package com.unicam.Repository;

import com.unicam.Model.Contenuto;

import java.util.ArrayList;
import java.util.List;

public class ContenutoRepository implements IContenutoRepository{

    private List<Contenuto> contenuti = new ArrayList<Contenuto>();



    @Override
    public void save(Contenuto contenuto) {

        contenuti.add(contenuto);
    }

    @Override
    public List<Contenuto> findAll() {
        return contenuti;
    }

    @Override
    public Contenuto findById(String id) {
        for (Contenuto contenuto : contenuti) {
            if (contenuto.getId().equals(id)) {
                return contenuto;
            }
        }
        return null;
    }

    @Override
    public void delete(String id){
        for (Contenuto contenuto : contenuti) {
            if (contenuto.getId().equals(id)) {
                contenuti.remove(contenuto);
            }
        }
        System.out.println("Contenuto rimosso con successo");
    }





}
