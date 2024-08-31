package com.unicam.Repository;

import com.unicam.Model.Contenuto;
import com.unicam.Model.PuntoGeolocalizzato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IContenutoRepository extends JpaRepository<Contenuto, Long>{

    PuntoGeolocalizzato findByTitolo(String nome);
}
