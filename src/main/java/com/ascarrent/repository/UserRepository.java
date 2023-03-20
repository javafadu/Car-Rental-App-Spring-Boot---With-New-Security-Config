package com.ascarrent.repository;

import com.ascarrent.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
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

    @Modifying // to manipulate on data (DML) with a custom query
    @Query("UPDATE User u SET u.firstName=:firstName, u.lastName=:lastName, u.email=:email, u.phoneNumber=:phoneNumber," +
            "u.address=:address, u.zipCode=:zipCode, u.birthDate=:birthDate WHERE u.id=:id")
    void update(@Param("id") Long id,
                @Param("firstName") String firstName,
                @Param("lastName") String lastName,
                @Param("email") String email,
                @Param("phoneNumber") String phoneNumber,
                @Param("address") String address,
                @Param("zipCode") String zipCode,
                @Param("birthDate") LocalDate birthDate
                );




}
