package com.unicam.Repository;

import com.unicam.Model.Contenuto;

import java.util.ArrayList;
import java.util.List;

public class ContenutoRepository<T extends Contenuto> implements IContenutoRepository {

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
    public T findById(String id) {
        for (T contenuto : contenuti) {
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
