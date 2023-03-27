package com.ascarrent.domain;


import com.ascarrent.domain.enums.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Table(name = "tbl_reservation")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private Car car;

    @OneToOne
    private User user;

    @Column(nullable = false)
    private LocalDateTime pickUpTime;

    @Column(nullable = false)
    private LocalDateTime dropOffTime;

    @Column(length =150, nullable = false)
    private String pickUpLocation;

    @Column(length =150, nullable = false)
    private String dropOffLocation;

    @Enumerated(EnumType.STRING)
    @Column(length = 30, nullable = false)
    private ReservationStatus status;

    @Column(nullable = false)
    private Double totalPrice;

}
