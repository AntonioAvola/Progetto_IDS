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
    public Contenuto findById(Long id) {
        for (Contenuto contenuto : contenuti) {
            if (contenuto.getId() == id) {
                return contenuto;
            }
        }
        return null;
    }

    @Override
    public void delete(Long id){
        for (Contenuto contenuto : contenuti) {
            if (contenuto.getId() == id) {
                contenuti.remove(contenuto);
            }
        }
        System.out.println("Contenuto rimosso con successo");
    }





}
