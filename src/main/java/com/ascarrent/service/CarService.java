package com.ascarrent.service;

import com.ascarrent.domain.Car;
import com.ascarrent.domain.ImageFile;
import com.ascarrent.dto.CarDTO;
import com.ascarrent.exception.BadRequestException;
import com.ascarrent.exception.ConflictException;
import com.ascarrent.exception.ResourceNotFoundException;
import com.ascarrent.exception.message.ErrorMessages;
import com.ascarrent.mapper.CarMapper;
import com.ascarrent.repository.CarRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CarService {

    private final CarRepository carRepository;
    private final ImageFileService imageFileService;
    private final CarMapper carMapper;


    public CarService(CarRepository carRepository, ImageFileService imageFileService, CarMapper carMapper) {
        this.carRepository = carRepository;
        this.imageFileService = imageFileService;
        this.carMapper = carMapper;
    }

    public void saveCar(String imageId, CarDTO carDTO) {
        // check1: if the imageId exists or not
        ImageFile imageFile = imageFileService.findImageById(imageId);

        // check2: if this image is belongs to another car or not
        Integer usedCarCount = carRepository.findCarCountByImageId(imageFile.getId());
        if(usedCarCount>0) {
            throw new ConflictException(ErrorMessages.IMAGE_USED_MESSAGE);
        }

        // Mapping CarDTO to Car
        Car car = carMapper.carDTOToCar(carDTO);
        // get image and set into car object
        Set<ImageFile> imFiles = new HashSet<>();
        imFiles.add(imageFile);
        car.setImage(imFiles);

        // Save Car
        carRepository.save(car);
    }


    public List<CarDTO> getAllCars() {
       List<Car> carList = carRepository.findAll();
       // mapping List<Car> to List<CarDTO>
       return carMapper.carListToCarDTOList(carList);
    }

    public Page<CarDTO> getAllCarsWithPages(Pageable pageable) {
        Page<Car> carWithPages = carRepository.findAll(pageable);
        return carWithPages.map(car->carMapper.carToCarDTO(car));
    }

    public CarDTO getCarById(Long id) {
        return carMapper.carToCarDTO(getCar(id));
    }




    public void updateCar(Long id, String imageId, CarDTO carDTO) {
        // Check1: car exists with id or not
        Car car = getCar(id);

        // Check2: car is builtIn or not
        if(car.getBuiltIn()) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        // Check3: image is already used for another car or not
        ImageFile imageFile = imageFileService.findImageById(imageId);
        List<Car> carList = carRepository.findCarsByImageId(imageFile.getId());
        for (Car c:carList
             ) {
            // Long --> long
            if(car.getId().longValue()!=c.getId().longValue()) {
                throw new ConflictException(ErrorMessages.IMAGE_USED_MESSAGE);
            }
        }

        // Set all fields
        car.setYear(carDTO.getYear());
        car.setDoors(carDTO.getDoors());
        car.setFuelType(carDTO.getFuelType());
        car.setLuggage(carDTO.getLuggage());
        car.setModel(carDTO.getModel());
        car.setPricePerHour(carDTO.getPricePerHour());
        car.setSeats(carDTO.getSeats());
        car.setTransmission(carDTO.getTransmission());

        car.getImage().add(imageFile);

        carRepository.save(car);

    }

    public void deleteCarWithId(Long id) {
        // Check1: car exists with id or not
        Car car = getCar(id);

        // Check2: car is builtIn or not
        if(car.getBuiltIn()) {
            throw new BadRequestException(ErrorMessages.NOT_PERMITTED_METHOD_MESSAGE);
        }

        carRepository.delete(car);
    }



    // Support Methods

    private Car getCar(Long id) {
        Car car = carRepository.findCarById(id).orElseThrow(()-> new ResourceNotFoundException(String.format(ErrorMessages.RESOURCE_NOT_FOUND_EXCEPTION,id)));
        return car;
    }
}
