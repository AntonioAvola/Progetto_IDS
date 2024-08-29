package com.unicam.Repository;

import com.unicam.Model.Contenuto;

import java.util.ArrayList;
import java.util.List;

public class ContenutoRepository<T extends Contenuto> implements IContenutoRepository<T> {

    private List<T> contenuti = new ArrayList<T>();

    @Override
    public void add(T contenuto){
        this.contenuti.add(contenuto);
    }

    @Override
    public List<T> findAll() {
        return contenuti;
    }


    @Override
    public T findById(int id) {
        for (T contenuto : contenuti) {
            if (contenuto.getId() == id) {
                return contenuto;
            }
        }
        return null;
    }

    @Override
    public void delete(int id){
        for (Contenuto contenuto : contenuti) {
            if (contenuto.getId() == id) {
                contenuti.remove(contenuto);
            }
        }
        System.out.println("Contenuto rimosso con successo");
    }

}
