package com.ascarrent.controller;

import com.ascarrent.domain.Car;
import com.ascarrent.domain.User;
import com.ascarrent.dto.request.ReservationRequest;
import com.ascarrent.dto.response.ACRResponse;
import com.ascarrent.dto.response.ResponseMessage;
import com.ascarrent.service.CarService;
import com.ascarrent.service.ReservationService;
import com.ascarrent.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationService reservationService;
    private final CarService carService;
    private final UserService userService;

    public ReservationController(ReservationService reservationService, CarService carService, UserService userService) {
        this.reservationService = reservationService;
        this.carService = carService;
        this.userService = userService;
    }

    // -- make Reservation
    @PostMapping("/add")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> makeReservation(
            @RequestParam("carId") Long carId,
            @Valid @RequestBody ReservationRequest reservationRequest) {

        Car car = carService.getCarWithId(carId);
        User user = userService.getCurrentLoggedInUser();

        reservationService.createReservation(reservationRequest, user,car);
        ACRResponse response = new ACRResponse(ResponseMessage.RESERVATION_CREATED_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    // -- Make Reservation by Admin for a specific user
    @PostMapping("/add/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> addReservation(
            @RequestParam("userId") Long userId,
            @RequestParam("carId") Long carId,
            @Valid @RequestBody ReservationRequest reservationRequest) {

        Car car = carService.getCarWithId(carId);
        User user = userService.getById(userId);
        reservationService.createReservation(reservationRequest,user,car);
        ACRResponse response = new ACRResponse(ResponseMessage.RESERVATION_CREATED_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }


}
