package com.ascarrent.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 30, nullable = false)
    private String model;

    @Column(nullable = false)
    private Integer doors;

    @Column(nullable = false)
    private Integer seats;

    @Column(nullable = false)
    private Integer luggage;

    @Column(length = 30, nullable = false)
    private String transmission;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Double pricePerHour;

    @Column(length = 30, nullable = false)
    private String fuelType;

    private Boolean builtIn = false;

    @OneToMany(orphanRemoval = true) // what we do in parent, it affects child.
    @JoinColumn(name = "car_id")
    private Set<ImageFile> image;


}
