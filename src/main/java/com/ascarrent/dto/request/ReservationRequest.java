package com.ascarrent.dto.request;

import com.ascarrent.domain.Car;
import com.ascarrent.domain.User;
import com.ascarrent.domain.enums.ReservationStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationRequest {


    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm")
    @NotNull(message = "Please provide the pickup time of the reservation")
    private LocalDateTime pickUpTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy HH:mm")
    @NotNull(message = "Please provide the drop off time of the reservation")
    private LocalDateTime dropOffTime;

    @Size(max=150, message = "Pick up location must be max 150 character")
    @NotBlank(message = "Please provide the pick up location of the reservation")
    private String pickUpLocation;

    @Size(max=150, message = "Drop off location must be max 150 character")
    @NotBlank(message = "Please provide the drop off location of the reservation")
    private String dropOffLocation;



}
