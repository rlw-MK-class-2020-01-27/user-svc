package com.galvanize.repositories;

import com.galvanize.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
    Optional<User> findByEmail(String email);
}
