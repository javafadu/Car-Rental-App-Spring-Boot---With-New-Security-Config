package com.ascarrent.service;

import com.ascarrent.domain.Car;
import com.ascarrent.domain.Reservation;
import com.ascarrent.domain.User;
import com.ascarrent.domain.enums.ReservationStatus;
import com.ascarrent.dto.ReservationDTO;
import com.ascarrent.dto.request.ReservationRequest;
import com.ascarrent.dto.request.ReservationUpdateRequest;
import com.ascarrent.exception.BadRequestException;
import com.ascarrent.exception.ResourceNotFoundException;
import com.ascarrent.exception.message.ErrorMessages;
import com.ascarrent.mapper.ReservationMapper;
import com.ascarrent.repository.ReservationRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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




    public List<ReservationDTO> getAllReservations() {
       List<Reservation> reservations = reservationRepository.findAll();
       return reservationMapper.resListToResDTOList(reservations);
    }


    public Page<ReservationDTO> getAllWithPages(Pageable pageable) {
        Page<Reservation> reservationPage = reservationRepository.findAll(pageable);
        // lambda reference
        // return reservationPage.map((res->reservationMapper.reservationToReservationDTO(res)));
        // method reference
        return reservationPage.map(reservationMapper::reservationToReservationDTO);
    }

    public void updateReservation(Long reservationId, Car car, ReservationUpdateRequest reservationUpdateRequest) {
        Reservation reservation = getById(reservationId);

        // check reservation status code (if CANCELLED or DONE do not update)
        if(reservation.getStatus().equals(ReservationStatus.CANCELLED) ||
                reservation.getStatus().equals(ReservationStatus.DONE)) {
            throw new BadRequestException(ErrorMessages.RESERVATION_STATUS_CANT_CHANGE_MESSAGE);
        }

        // check: In case of status change to DONE or CANCELLED, do not check pick up time and drop off time
        if(reservationUpdateRequest.getStatus() != null &&
        reservationUpdateRequest.getStatus()==ReservationStatus.CREATED) {
            // check reservation times
            checkReservationTimeIsCorrect(reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());
            // get conflict reservations
            List<Reservation> conflictReservations = gerConflictReservation(car,reservationUpdateRequest.getPickUpTime(),reservationUpdateRequest.getDropOffTime());
            if(!conflictReservations.isEmpty()) {
                if(!(conflictReservations.size()==1 &&
                        conflictReservations.get(0).getId().equals(reservationId))) {
                    throw new BadRequestException(ErrorMessages.CAR_NOT_AVAILABLE_MESSAGE);
                }
            }
            // calculate total price
            Double totalPrice = getTotalPrice(car, reservationUpdateRequest.getPickUpTime(), reservationUpdateRequest.getDropOffTime());

            reservation.setTotalPrice(totalPrice);
            reservation.setCar(car);
        }

        reservation.setPickUpTime(reservationUpdateRequest.getPickUpTime());
        reservation.setDropOffTime(reservationUpdateRequest.getDropOffTime());
        reservation.setPickUpLocation(reservationUpdateRequest.getPickUpLocation());
        reservation.setDropOffLocation(reservationUpdateRequest.getDropOffLocation());
        reservation.setStatus(reservationUpdateRequest.getStatus());

        reservationRepository.save(reservation);
    }


    public ReservationDTO getReservationDTO(Long id) {
        Reservation reservation = getById(id);
        return  reservationMapper.reservationToReservationDTO(reservation);
    }


    public Page<ReservationDTO> findReservationPageByUser(User user, Pageable pageable) {
        Page<Reservation> reservationPage =reservationRepository.findAllByUser(user,pageable);
        return reservationPage.map(reservationMapper::reservationToReservationDTO);
    }




    // *********** Supporting Methods **********

    // Validate1:  dates are right else throw exception
    public void checkReservationTimeIsCorrect(LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
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
    public Boolean checkCarAvailability(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        List<Reservation> existReservations =  gerConflictReservation(car, pickUpTime, dropOffTime);
        return existReservations.isEmpty();
    }


    // Calculate Total Price
    public Double getTotalPrice(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        Long minutes = ChronoUnit.MINUTES.between(pickUpTime,dropOffTime);
        double hours = Math.ceil(minutes/60.0);

        return car.getPricePerHour()*hours;

    }


    // Validate3: conflict of reservations (pick up time, drop off time check with another res)
    public List<Reservation> gerConflictReservation(Car car, LocalDateTime pickUpTime, LocalDateTime dropOffTime) {
        //
        if(pickUpTime.isAfter(dropOffTime)) {
            throw new BadRequestException(ErrorMessages.RESERVATION_TIME_INCORRECT_MESSAGE);
        }

        ReservationStatus[] status = {ReservationStatus.CANCELLED, ReservationStatus.DONE};

        List<Reservation> existReservation = reservationRepository.checkCarStatus(car.getId(), pickUpTime, dropOffTime, status);
        return existReservation;
    }

    // Get Reservation by id
    public Reservation getById(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElseThrow(()->new ResourceNotFoundException(String.format(ErrorMessages.RESOURCE_NOT_FOUND_EXCEPTION,id)));
        return reservation;
    }



}
