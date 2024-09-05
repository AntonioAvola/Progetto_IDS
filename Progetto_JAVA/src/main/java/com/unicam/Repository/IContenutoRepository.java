package com.unicam.Repository;

import com.unicam.Model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IContenutoRepository<T extends Contenuto> extends JpaRepository<T, Long>{

    PuntoGeolocalizzato findGeoByTitolo(String nome);
    PuntoLogico findLogicoByTitolo(String toUpperCase);
    Itinerario findItinerarioByTitolo(String toUpperCase);
    Evento findEventoByTitolo(String toUpperCase);
    Contest findContestByTitolo(String toUpperCase);

    List<PuntoGeolocalizzato> findPuntoGeoByComune(String comune);

    List<PuntoLogico> findPuntoLogicoByComune(String comune);

    List<Itinerario> findItinerarioByComune(String comune);

    List<Evento> findEventoByComune(String comune);

    List<Contest> findContestByComune(String comune);
}
