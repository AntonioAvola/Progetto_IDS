package com.unicam.Repository.Contenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntoGeoRepository extends JpaRepository<PuntoGeolocalizzato, Long> {

    PuntoGeolocalizzato findGeoByTitolo(String nome);

    List<PuntoGeolocalizzato> findPuntoGeoByComune(String comune);
}
