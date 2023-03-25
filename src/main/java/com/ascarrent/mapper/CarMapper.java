package com.ascarrent.mapper;

import com.ascarrent.domain.Car;
import com.ascarrent.domain.ImageFile;
import com.ascarrent.dto.CarDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring") // I can inject below objects in any class, give control to the spring
public interface CarMapper {

    //  CarDTO -> Car
    @Mapping(target="image", ignore = true)
    Car carDTOToCar(CarDTO carDTO);

    // List<Car> -> List<CarDTO>
    // Set<ImageFile> -> Set<String> different
    // automatically mapping based on the below single mapping
    List<CarDTO> carListToCarDTOList(List<Car> cars);

    // Car -> CarDTO
    @Mapping(source = "image", target ="image", qualifiedByName = "getImageAsString")
    CarDTO carToCarDTO(Car car);

    @Named("getImageAsString")
    public static Set<String> getImageIds(Set<ImageFile> imageFiles) {
        Set<String> imgs = new HashSet<>();
        imgs = imageFiles.stream().map(imFile->imFile.getId().toString()).collect(Collectors.toSet());
        return  imgs;
    }
}
