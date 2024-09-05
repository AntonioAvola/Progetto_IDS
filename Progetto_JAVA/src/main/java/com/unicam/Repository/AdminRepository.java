package com.unicam.Repository;

import com.unicam.Model.AdminPiattaforma;
import com.unicam.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminPiattaforma, Long> {
    AdminPiattaforma findByUsername(String username);

    AdminPiattaforma findByEmail(String email);
}
