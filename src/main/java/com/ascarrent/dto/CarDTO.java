package com.ascarrent.dto;

import com.ascarrent.domain.ImageFile;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {

    private Long id;

    @Size(max=30, message = "Size is exceeded")
    @NotBlank(message = "Please provide car model")
    private String model;

    @NotNull(message = "Please provide door numbers")
    private Integer doors;

    @NotNull(message = "Please provide seat numbers")
    private Integer seats;

    @NotNull(message = "Please provide luggage capacity")
    private Integer luggage;

    @NotBlank(message = "Please provide car transmission info")
    private String transmission;

    @NotNull(message = "Please provide car production year")
    private Integer year;

    @NotNull(message = "Please provide price per hour")
    private Double pricePerHour;

    @Size(max=30, message = "Size is exceeded")
    @NotBlank(message = "Please provide car fuel type")
    private String fuelType;

    private Boolean builtIn = false;

    private Set<String> image;

}
