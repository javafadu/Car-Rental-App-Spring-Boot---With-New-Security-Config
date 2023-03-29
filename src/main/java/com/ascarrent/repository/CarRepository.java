package com.ascarrent.repository;

import com.ascarrent.domain.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {


    // JPQL
    @Query("SELECT count(*) FROM Car c JOIN c.image img WHERE img.id = :id")
    Integer findCarCountByImageId(@Param("id") String id);

    @Override
    @EntityGraph(attributePaths = {"image"}) // set it as EAGER
    List<Car> findAll();

    @EntityGraph(attributePaths = {"image"}) // set it as EAGER
    Page<Car> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"image"}) // set it as EAGER
    Optional<Car> findCarById(Long id);

    @Query("SELECT c FROM Car c JOIN c.image im WHERE im.id=:id")
    List<Car> findCarsByImageId(@Param("id") String id);

    @EntityGraph(attributePaths = "id") // only brings id level field not sub fields
    List<Car> getAllBy();
}
