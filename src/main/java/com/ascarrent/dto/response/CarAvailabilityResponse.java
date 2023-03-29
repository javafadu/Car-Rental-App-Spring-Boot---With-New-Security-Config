package com.ascarrent.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CarAvailabilityResponse extends ACRResponse{

    private Boolean available;
    private Double totalPrice;

    // All Arg Constructor
    public CarAvailabilityResponse(String message, Boolean success, Boolean available, Double totalPrice) {
        super(message,success);
        this.available=available;
        this.totalPrice=totalPrice;
    }

}
