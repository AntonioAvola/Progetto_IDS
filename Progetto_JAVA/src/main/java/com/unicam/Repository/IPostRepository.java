package com.unicam.Repository;

import com.unicam.Model.PostTurista;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPostRepository extends JpaRepository<PostTurista, Long> {
}
