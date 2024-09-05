package com.unicam.Repository.Contenuto;

import com.unicam.Model.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    Evento findEventoByTitolo(String toUpperCase);

    List<Evento> findEventoByComune(String comune);
}
