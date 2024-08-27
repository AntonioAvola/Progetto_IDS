package com.unicam.Repository;

import com.unicam.Controller.ContenutoController;
import com.unicam.Model.Contenuto;

import java.util.List;

public interface IContenutoRepository {

    void save(Contenuto contenuto);

    Contenuto findById(Long id);

    List<Contenuto> findAll();

    void delete(Long ID);

}
