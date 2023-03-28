package com.ascarrent.service;

import com.ascarrent.domain.Car;
import com.ascarrent.domain.Reservation;
import com.ascarrent.domain.User;
import com.ascarrent.domain.enums.ReservationStatus;
import com.ascarrent.dto.request.ReservationRequest;
import com.ascarrent.exception.BadRequestException;
import com.ascarrent.exception.message.ErrorMessages;
import com.ascarrent.mapper.ReservationMapper;
import com.ascarrent.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    public ReservationService(ReservationRepository reservationRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    public void createReservation(ReservationRequest reservationRequest, User user, Car car) {

        // Check1: reservation times validation
        checkReservationTimeIsCorrect(reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());

        // Check2: car availability
        boolean carStatus = checkCarAvailability(car, reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());

        // Mapping ReservationRequest to Reservation
        Reservation reservation = reservationMapper.reservationRequestToReservation(reservationRequest);

        // Set other fields outside the mapping
        if(carStatus) {
            reservation.setStatus(ReservationStatus.CREATED);
        } else {
            throw new BadRequestException(ErrorMessages.CAR_NOT_AVAILABLE_MESSAGE);
        }

        reservation.setCar(car);
        reservation.setUser(user);
        Double totalPrice = getTotalPrice(car,reservationRequest.getPickUpTime(), reservationRequest.getDropOffTime());
        reservation.setTotalPrice(totalPrice);

        // save
        reservationRepository.save(reservation);
    }








    // *********** Supporting Methods **********

    // Validate1:  dates are right else throw exception
    private void checkReservationTimeIsCorrect(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        LocalDateTime now = LocalDateTime.now();

        if (pickUpTime.isBefore(now)) {
            throw new BadRequestException(ErrorMessages.RESERVATION_TIME_INCORRECT_MESSAGE);
        }

        boolean isEqual = pickUpTime.isEqual(dropOffTime);
        boolean isBefore = pickUpTime.isBefore(dropOffTime);
        if (isEqual || !isBefore) {
            throw new BadRequestException(ErrorMessages.RESERVATION_TIME_INCORRECT_MESSAGE);
        }
    }

    // Validate2:  car is available or not
    private Boolean checkCarAvailability(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        List<Reservation> existReservations =  gerConflictReservation(car, pickUpTime, dropOffTime);
        return existReservations.isEmpty();
    }


    // Calculate Total Price
    private Double getTotalPrice(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        Long minutes = ChronoUnit.MINUTES.between(pickUpTime,dropOffTime);
        double hours = Math.ceil(minutes/60.0);

        return car.getPricePerHour()*hours;

    }


    // Validate3: conflict of reservations (pick up time, drop off time check with another res)
    private List<Reservation> gerConflictReservation(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        //
        if(pickUpTime.isAfter(dropOffTime)) {
            throw new BadRequestException(ErrorMessages.RESERVATION_TIME_INCORRECT_MESSAGE);
        }

        ReservationStatus[] status = {ReservationStatus.CANCELLED, ReservationStatus.DONE};

        List<Reservation> existReservation = reservationRepository.checkCarStatus(car.getId(), pickUpTime, dropOffTime, status);
        return existReservation;
    }


}
