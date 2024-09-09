package com.unicam.Repository.Contenuto;

import com.unicam.Model.Contest;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

    Contest findContestByTitolo(String toUpperCase);

    List<Contest> findContestByComune(String comune);

    List<Contest> findByAutore(User user);

    boolean existsByTitoloAndComune(String titolo, String comune);

    Contest findContestByTitoloAndComune(String nomeContenuto, String comune);

    boolean existsByTitoloAndComuneAndStato(String nomeContenuto, String comune, StatoContenuto approvato);

    List<Contest> findByComuneAndStato(String nomeComune, StatoContenuto approvato);
}
