package com.unicam.Repository.Contenuto;

import com.unicam.Model.PuntoLogico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PuntoLogicoRepository extends JpaRepository<PuntoLogico, Long> {

    PuntoLogico findLogicoByTitolo(String toUpperCase);

    List<PuntoLogico> findPuntoLogicoByComune(String comune);
}
