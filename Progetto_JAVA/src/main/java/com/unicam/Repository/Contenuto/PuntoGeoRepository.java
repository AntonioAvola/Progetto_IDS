package com.unicam.Repository.Contenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PuntoGeoRepository extends JpaRepository<PuntoGeolocalizzato, Long> {

    PuntoGeolocalizzato findGeoByTitolo(String nome);

    List<PuntoGeolocalizzato> findPuntoGeoByComune(String comune);

    List<PuntoGeolocalizzato> findByAutore(User user);

    PuntoGeolocalizzato findGeoByTitoloAndComune(String comune, String nomeComune);

    boolean existsByTitoloAndComune(String titolo, String comune);

    boolean existsByLatitudineAndLongitudine(Double latitudine, Double longitudine);

    List<PuntoGeolocalizzato> findByTitoloAndComune(String titolo, String comune);

    List<PuntoGeolocalizzato> findByComune(String comune);

    boolean existsByTitoloAndComuneAndStato(String titolo, String comune, StatoContenuto approvato);

    List<PuntoGeolocalizzato> findByLatitudineAndLongitudineAndStato(Double latitudine, Double longitudine, StatoContenuto stato);

    List<PuntoGeolocalizzato> findByTitoloAndComuneAndStato(String nomeContenuto, String comune, StatoContenuto attesa);

    List<PuntoGeolocalizzato> findByComuneAndStato(String comune, StatoContenuto attesa);

    List<PuntoGeolocalizzato> findByStato(StatoContenuto attesa);
}
