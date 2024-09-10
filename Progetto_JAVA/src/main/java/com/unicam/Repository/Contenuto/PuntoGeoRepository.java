package com.unicam.Repository.Contenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntoGeoRepository extends JpaRepository<PuntoGeolocalizzato, Long> {

    List<PuntoGeolocalizzato> findPuntoGeoByComune(String comune);

    List<PuntoGeolocalizzato> findByAutore(User user);

    PuntoGeolocalizzato findGeoByTitoloAndComune(String titolo, String nomeComune);

    boolean existsByTitoloAndComune(String titolo, String comune);

    List<PuntoGeolocalizzato> findByTitoloAndComune(String titolo, String comune);

    boolean existsByTitoloAndComuneAndStato(String titolo, String comune, StatoContenuto approvato);

    List<PuntoGeolocalizzato> findByLatitudineAndLongitudineAndStato(Double latitudine, Double longitudine, StatoContenuto stato);

    List<PuntoGeolocalizzato> findByComuneAndStato(String comune, StatoContenuto attesa);

    PuntoGeolocalizzato findByTitoloAndComuneAndStato(String nomePuntoGeo, String comune, StatoContenuto approvato);
}
