package com.unicam.Repository;

import com.unicam.Model.PostTurista;
import com.unicam.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<PostTurista, Long> {
    List<PostTurista> findByAutore(User user);
}
