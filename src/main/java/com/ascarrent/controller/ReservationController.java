package com.ascarrent.controller;

import com.ascarrent.domain.Car;
import com.ascarrent.domain.User;
import com.ascarrent.dto.ReservationDTO;
import com.ascarrent.dto.request.ReservationRequest;
import com.ascarrent.dto.request.ReservationUpdateRequest;
import com.ascarrent.dto.response.ACRResponse;
import com.ascarrent.dto.response.CarAvailabilityResponse;
import com.ascarrent.dto.response.ResponseMessage;
import com.ascarrent.service.CarService;
import com.ascarrent.service.ReservationService;
import com.ascarrent.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservations")
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
    public ResponseEntity<ACRResponse> makeReservation(@RequestParam("carId") Long carId, @Valid @RequestBody ReservationRequest reservationRequest) {

        Car car = carService.getCarWithId(carId);
        User user = userService.getCurrentLoggedInUser();

        reservationService.createReservation(reservationRequest, user, car);
        ACRResponse response = new ACRResponse(ResponseMessage.RESERVATION_CREATED_RESPONSE_MESSAGE, true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    // -- Make Reservation by Admin for a specific user
    @PostMapping("/add/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> addReservation(@RequestParam("userId") Long userId, @RequestParam("carId") Long carId, @Valid @RequestBody ReservationRequest reservationRequest) {

        Car car = carService.getCarWithId(carId);
        User user = userService.getById(userId);
        reservationService.createReservation(reservationRequest, user, car);
        ACRResponse response = new ACRResponse(ResponseMessage.RESERVATION_CREATED_RESPONSE_MESSAGE, true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // -- Get All Reservations
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ReservationDTO>> getAllReservations() {
        List<ReservationDTO> allReservations = reservationService.getAllReservations();
        return ResponseEntity.ok(allReservations);
    }

    // -- Get All Reservations With Pages
    @GetMapping("/admin/all/pages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservationDTO>> getAllReservationsWithPage(@RequestParam(required = false, value = "page", defaultValue = "0") int page, @RequestParam(required = false, value = "size", defaultValue = "5") int size, @RequestParam(required = false, value = "sort", defaultValue = "id") String prop, @RequestParam(required = false, value = "direction", defaultValue = "ASC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<ReservationDTO> allReservations = reservationService.getAllWithPages(pageable);
        return ResponseEntity.ok(allReservations);

    }

    // -- Check car is available or not
    @GetMapping("/auth")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> checkCarIsAvailable(@RequestParam("carId") Long carId, @RequestParam("pickUpTime") @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm") LocalDateTime pickUpTime, @RequestParam("dropOffTime") @DateTimeFormat(pattern = "MM/dd/yyyy HH:mm") LocalDateTime dropOffTime) {

        Car car = carService.getCarWithId(carId);
        Boolean isAvailable = reservationService.checkCarAvailability(car, pickUpTime, dropOffTime);
        Double totalPrice = reservationService.getTotalPrice(car, pickUpTime, dropOffTime);

        ACRResponse response = new CarAvailabilityResponse(ResponseMessage.CAR_AVAILABLE_RESPONSE_MESSAGE, true, isAvailable, totalPrice);
        return ResponseEntity.ok(response);
    }

    // -- Update a Reservation
    @PutMapping("/admin/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> updateReservation(@RequestParam("carId") Long carId, @RequestParam("reservationId") Long reservationId, @Valid @RequestBody ReservationUpdateRequest reservationUpdateRequest) {
        Car car = carService.getCarWithId(carId);
        reservationService.updateReservation(reservationId, car, reservationUpdateRequest);
        ACRResponse response = new ACRResponse(ResponseMessage.RESERVATION_UPDATED_RESPONSE_MESSAGE, true);
        return ResponseEntity.ok(response);
    }

    // -- Get Reservation By Id
    @GetMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> getReservationById(@PathVariable Long id) {
        ReservationDTO reservationDTO = reservationService.getReservationDTO(id);
        return ResponseEntity.ok(reservationDTO);
    }

    // -- Get Reservation for specific user
    @GetMapping("/admin/auth/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ReservationDTO>> getAllUserReservations(@RequestParam(required = false, value = "userId", defaultValue = "0") Long userId, @RequestParam(required = false, value = "page", defaultValue = "0") int page, @RequestParam(required = false, value = "size", defaultValue = "5") int size, @RequestParam(required = false, value = "sort", defaultValue = "id") String prop, @RequestParam(required = false, value = "direction", defaultValue = "ASC") Sort.Direction direction) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        User user = userService.getById(userId);
        Page<ReservationDTO> reservationDTOS = reservationService.findReservationPageByUser(user, pageable);

        return ResponseEntity.ok(reservationDTOS);
    }

    // -- Get Own (Logged in user) Reservation With Id
    @GetMapping("/{id}/auth")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<ReservationDTO> getUserReservationById(@PathVariable Long id) {
        User user = userService.getCurrentLoggedInUser();
        ReservationDTO reservationDTO = reservationService.findByIdAndUser(id,user);
        return ResponseEntity.ok(reservationDTO);
    }

    // Get Own All Reservation for Logged In user
    @GetMapping("/auth/all")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    public ResponseEntity<Page<ReservationDTO>> getAuthAllReservations(
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "5") int size,
            @RequestParam(required = false, value = "sort", defaultValue = "id") String prop,
            @RequestParam(required = false, value = "direction", defaultValue = "ASC") Sort.Direction direction
    ) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));

        User user = userService.getCurrentLoggedInUser();
        Page<ReservationDTO> reservationDTOPages = reservationService.findReservationPageByUser(user,pageable);
        return ResponseEntity.ok(reservationDTOPages);
    }

    // -- Delete Reservation
    @DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> deleteReservation(@PathVariable Long id) {
        reservationService.removeById(id);
        ACRResponse response = new ACRResponse(ResponseMessage.RESERVATION_DELETED_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

}
