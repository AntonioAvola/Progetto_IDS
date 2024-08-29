package com.unicam.Repository;

import com.unicam.Model.Contenuto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IContenutoRepository<T extends Contenuto> {

    void add(T contenuto);

    T findById(int id);

    List<T> findAll();

    void delete(int ID);

}
