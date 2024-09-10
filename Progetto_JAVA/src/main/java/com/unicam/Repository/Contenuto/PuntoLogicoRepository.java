package com.unicam.Repository.Contenuto;

import com.unicam.Model.PuntoGeolocalizzato;
import com.unicam.Model.PuntoLogico;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface PuntoLogicoRepository extends JpaRepository<PuntoLogico, Long> {

    List<PuntoLogico> findPuntoLogicoByComune(String comune);

    List<PuntoLogico> findByAutore(User user);

    boolean existsByTitoloAndComune(String titolo, String comune);

    List<PuntoLogico> findByTitoloAndComune(String titolo, String comune);

    List<PuntoLogico> findByTitoloAndComuneAndRiferimentoAndStato(String titolo, String comune, PuntoGeolocalizzato riferimento, StatoContenuto attesa);

    boolean existsByTitoloAndComuneAndStato(String nomeContenuto, String comune, StatoContenuto attesa);

    PuntoLogico findByTitoloAndRiferimento(String nomeContenuto, PuntoGeolocalizzato luogo);

    boolean existsByTitoloAndComuneAndRiferimento(String titolo, String comune, PuntoGeolocalizzato luogo);

    List<PuntoLogico> findByRiferimento(PuntoGeolocalizzato punto);

    List<PuntoLogico> findByComune(String comune);
}
