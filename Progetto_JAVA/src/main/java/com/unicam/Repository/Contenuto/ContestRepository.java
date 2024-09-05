package com.unicam.Repository.Contenuto;

import com.unicam.Model.Contest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContestRepository extends JpaRepository<Contest, Long> {

    Contest findContestByTitolo(String toUpperCase);

    List<Contest> findContestByComune(String comune);
}
