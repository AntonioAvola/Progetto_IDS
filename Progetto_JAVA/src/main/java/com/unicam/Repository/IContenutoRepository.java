package com.unicam.Repository;

import com.unicam.Model.Contenuto;

import java.util.List;

public interface IContenutoRepository<T extends Contenuto>{

    void add(T contenuto);

    T findById(String id);

    List<T> findAll();

    void delete(String ID);

}
