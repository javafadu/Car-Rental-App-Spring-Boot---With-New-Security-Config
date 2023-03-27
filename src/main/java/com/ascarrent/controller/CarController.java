package com.ascarrent.controller;

import com.ascarrent.dto.CarDTO;
import com.ascarrent.dto.response.ACRResponse;
import com.ascarrent.dto.response.ResponseMessage;
import com.ascarrent.service.CarService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/car")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }


    // -- Save Car
    @PostMapping("/admin/{imageId}/add")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> saveCar(
            @PathVariable String imageId,
            @Valid @RequestBody CarDTO carDTO){

        carService.saveCar(imageId,carDTO);
        ACRResponse response = new ACRResponse(ResponseMessage.CAR_SAVE_RESPONSE_MESSAGE,true);
        return new ResponseEntity<>(response,HttpStatus.CREATED);

    }

    // -- Get All Cars

}
