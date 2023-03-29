package com.ascarrent.repository;

import com.ascarrent.domain.Car;
import com.ascarrent.domain.Reservation;
import com.ascarrent.domain.User;
import com.ascarrent.domain.enums.ReservationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {


    // JPQL check 3 condition and find the conflict reservations
    @Query("SELECT r FROM Reservation r " +
            "JOIN FETCH Car c ON r.car=c.id WHERE " +
            "c.id=:carId AND (r.status NOT IN :status) AND :pickUpTime BETWEEN r.pickUpTime AND r.dropOffTime " +
            "OR " +
            "c.id=:carId AND (r.status NOT IN :status) AND :dropOffTime BETWEEN r.pickUpTime AND r.dropOffTime " +
            "OR " + // below: pick up time or drop off time , we choose pickUpTime
            "c.id=:carId AND (r.status NOT IN :status) AND (r.pickUpTime BETWEEN :pickUpTime AND :dropOffTime)")
    List<Reservation> checkCarStatus(@Param("carId") Long carId,
                                     @Param("pickUpTime") LocalDateTime pickUpTime,
                                     @Param("dropOffTime") LocalDateTime dropOffTime,
                                     @Param("status") ReservationStatus[] status);


    @EntityGraph(attributePaths = {"car", "car.image"})
        // bring car and car.image level fields, not others (do not bring ImageData)
    List<Reservation> findAll();

    @EntityGraph(attributePaths = {"car", "car.image"})
        // bring car and car.image level fields, not others (do not bring ImageData)
    Page<Reservation> findAll(Pageable pageable);

    @EntityGraph(attributePaths = {"car", "car.image","user"})
        // bring car , car.image and user level fields, not others (do not bring ImageData)
    Optional<Reservation> findById(Long aLong);

    @EntityGraph(attributePaths = {"car", "car.image","user"})
    Page<Reservation> findAllByUser(User user, Pageable pageable);

    @EntityGraph(attributePaths = {"car", "car.image","user"})
    Optional<Reservation> findByIdAndUser(Long id, User user);

    boolean existsByCar(Car car);

    boolean existsByUser(User user);

    @EntityGraph(attributePaths = {"car", "user"})
    List<Reservation> findAllBy();
}
