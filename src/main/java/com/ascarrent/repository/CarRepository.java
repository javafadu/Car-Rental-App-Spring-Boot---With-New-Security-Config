package com.ascarrent.repository;

import com.ascarrent.domain.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car,Long> {


    // JPQL
    @Query("SELECT count(*) FROM Car c JOIN c.image img WHERE img.id = :id")
    Integer findCarCountByImageId(@Param("id") String id);
}
