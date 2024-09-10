package com.unicam.Repository.Contenuto;

import com.unicam.Model.Itinerario;
import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItinerarioRepository extends JpaRepository<Itinerario, Long> {

    List<Itinerario> findItinerarioByComune(String comune);

    List<Itinerario> findByAutore(User user);

    Itinerario findItinerarioByTitoloAndComune(String nomeContenuto, String comune);

    //TODO eliminare
    Itinerario findByTitoloAndComune(String titolo, String comune);

    List<Itinerario> findByComuneAndStato(String comune, StatoContenuto attesa);

    boolean existsByTitoloAndComuneAndStato(String nomeContenuto, String comune, StatoContenuto attesa);
}
