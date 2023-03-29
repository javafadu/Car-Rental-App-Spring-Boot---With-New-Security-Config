package com.ascarrent.mapper;

import com.ascarrent.domain.ImageFile;
import com.ascarrent.domain.Reservation;
import com.ascarrent.domain.User;
import com.ascarrent.dto.ReservationDTO;
import com.ascarrent.dto.request.ReservationRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring") // I can inject below objects in any class, give control to the spring
public interface ReservationMapper {

    Reservation reservationRequestToReservation(ReservationRequest reservationRequest);

    @Mapping(source = "car.image", target = "car.image", qualifiedByName = "getImageAsString")
    @Mapping(source = "user", target = "userId", qualifiedByName = "getUserId")
    ReservationDTO reservationToReservationDTO(Reservation reservation);

    // single mapping is defined above, spring can map List<Reservation> based on above single mapping
    // firstly we need to define single mapping function
    List<ReservationDTO> resListToResDTOList(List<Reservation> reservationList);



    @Named("getImageAsString")
    public static Set<String> getImageIds(Set<ImageFile> imFiles) {
        Set<String> imgs =  new HashSet<>();
        imgs = imFiles.stream().map(imFile->imFile.getId().toString()).collect(Collectors.toSet());
        return imgs;
    }

    @Named("getUserId")
    public static Long getUserId(User user) {
        return user.getId();
    }


}
