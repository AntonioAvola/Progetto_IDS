package com.unicam.Repository.Contenuto;

import com.unicam.Model.Itinerario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

    Itinerario findItinerarioByTitolo(String toUpperCase);

    List<Itinerario> findItinerarioByComune(String comune);

}
