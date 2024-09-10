package com.unicam.Repository;

import com.unicam.Model.Comune;
import com.unicam.Model.StatoContenuto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ComuneRepository extends JpaRepository<Comune, Long> {
    boolean existsByNome(String nome);

    List<Comune> findAllByStatoRichiesta(StatoContenuto attesa);

    Comune findByNome(String nomeComune);
}
