package com.unicam.Repository;

import com.unicam.Controller.ContenutoController;
import com.unicam.Model.Contenuto;

import java.util.List;

public interface IContenutoRepository {

    void save(Contenuto contenuto);

    Contenuto findById(String id);

    List<Contenuto> findAll();

    void delete(String ID);

}
