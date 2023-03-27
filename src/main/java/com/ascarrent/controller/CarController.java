package com.ascarrent.controller;

import com.ascarrent.dto.CarDTO;
import com.ascarrent.dto.response.ACRResponse;
import com.ascarrent.dto.response.ResponseMessage;
import com.ascarrent.service.CarService;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.awt.geom.Area;
import java.util.List;

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
            @Valid @RequestBody CarDTO carDTO) {

        carService.saveCar(imageId, carDTO);
        ACRResponse response = new ACRResponse(ResponseMessage.CAR_SAVE_RESPONSE_MESSAGE, true);
        return new ResponseEntity<>(response, HttpStatus.CREATED);

    }

    // -- Get All Cars
    @GetMapping("/visitors/all")
    public ResponseEntity<List<CarDTO>> getAllCars() {
        List<CarDTO> cars = carService.getAllCars();
        return ResponseEntity.ok(cars);
    }

    // -- Get All Cars With Pages
    @GetMapping("/visitors/pages")
    public ResponseEntity<Page<CarDTO>> getAllCarsWithPages(
            @RequestParam(required = false, value = "page", defaultValue = "0") int page,
            @RequestParam(required = false, value = "size", defaultValue = "5") int size,
            @RequestParam(required = false, value = "sort", defaultValue = "id") String prop,
            @RequestParam(required = false, value = "direction", defaultValue = "ASC") Sort.Direction direction
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, prop));
        Page<CarDTO> carPages = carService.getAllCarsWithPages(pageable);
        return ResponseEntity.ok(carPages);
    }

    // -- Get Car by Id
    @GetMapping("visitors/{id}")
    public ResponseEntity<CarDTO> getCarById(@PathVariable Long id) {
        CarDTO carDTO = carService.getCarById(id);
        return ResponseEntity.ok(carDTO);
    }

    // --Update Car With ImageId and CarId
    @PutMapping("/admin/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> updateCar(
            @RequestParam("id") Long id,
            @RequestParam("imageId") String imageId,
            @RequestBody CarDTO carDTO) {

        carService.updateCar(id, imageId,carDTO);
        ACRResponse response = new ACRResponse(ResponseMessage.CAR_UPDATE_RESPONSE_MESSAGE,true);

        return ResponseEntity.ok(response);
    }

    // -- Delete Car
    @DeleteMapping("/admin/{id}/auth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> deleteCarWithId(@PathVariable Long id) {
        carService.deleteCarWithId(id);
        ACRResponse response = new ACRResponse(ResponseMessage.CAR_DELETE_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }

}
