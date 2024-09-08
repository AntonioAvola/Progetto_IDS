package com.unicam.Repository.Contenuto;

import com.unicam.Model.Evento;
import com.unicam.Model.StatoContenuto;
import com.unicam.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EventoRepository extends JpaRepository<Evento, Long> {

    Evento findEventoByTitolo(String toUpperCase);

    List<Evento> findEventoByComune(String comune);

    List<Evento> findByAutore(User user);

    Evento findEventoByTitoloAndComune(String nomeContenuto, String comune);

    boolean existsByTitoloAndComune(String nomeContenuto, String comune);

    List<Evento> findEventoByComuneAndStato(String comune, StatoContenuto attesa);

    boolean existsByTitoloAndComuneAndStato(String nomeContenuto, String comune, StatoContenuto approvato);
}
