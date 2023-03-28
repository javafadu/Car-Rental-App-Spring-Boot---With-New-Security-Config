package com.ascarrent.mapper;

import com.ascarrent.domain.Reservation;
import com.ascarrent.dto.request.ReservationRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring") // I can inject below objects in any class, give control to the spring
public interface ReservationMapper {

    Reservation reservationRequestToReservation(ReservationRequest reservationRequest);
}
