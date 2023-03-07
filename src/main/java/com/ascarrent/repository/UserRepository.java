package com.ascarrent.repository;

import com.ascarrent.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    Boolean existsByEmail(String email);

    // makes EAGER of roles (default is LAZY) for this method
    @EntityGraph(attributePaths = "roles")
    Optional<User> findByEmail(String email);

    // To get role details, we define roles as EAGER
    @EntityGraph(attributePaths = "roles")
    List<User> findAll();

    // To get role details, we define roles as EAGER
    @EntityGraph(attributePaths = "roles")
    Page<User> findAll(Pageable pageable);

    @EntityGraph(attributePaths = "roles")
    Optional<User> findById(Long id);



}
