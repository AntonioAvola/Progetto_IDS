package com.unicam.Repository;

import com.unicam.Model.Comune;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IComuneRepository extends JpaRepository<Comune, Long> {
}
