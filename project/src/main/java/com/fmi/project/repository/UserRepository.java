package com.fmi.project.repository;

import com.fmi.project.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByUsername(final String username);

    //TODO: findFirstByEmail
    //TODO: in application.properties to add configurations required for using Gmail SMTP server
    Optional<User> findFirstByEmail(final String email);
}
