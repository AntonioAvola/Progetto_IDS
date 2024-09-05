package com.unicam.Repository.Contenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntoGeoRepository extends JpaRepository<PuntoGeolocalizzato, Long> {

    PuntoGeolocalizzato findGeoByTitolo(String nome);

    List<PuntoGeolocalizzato> findPuntoGeoByComune(String comune);

    List<PuntoGeolocalizzato> findByAutore(User user);
}
