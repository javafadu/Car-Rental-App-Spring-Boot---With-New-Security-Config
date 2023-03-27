package com.ascarrent.service;

import com.ascarrent.domain.Car;
import com.ascarrent.domain.ImageFile;
import com.ascarrent.dto.CarDTO;
import com.ascarrent.exception.ConflictException;
import com.ascarrent.exception.message.ErrorMessages;
import com.ascarrent.mapper.CarMapper;
import com.ascarrent.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.HashSet;
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
}
