package com.ascarrent.service;

import com.ascarrent.domain.ImageData;
import com.ascarrent.domain.ImageFile;
import com.ascarrent.dto.ImageFileDTO;
import com.ascarrent.exception.ResourceNotFoundException;
import com.ascarrent.exception.message.ErrorMessages;
import com.ascarrent.repository.ImageFileRepository;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    public ImageFileService(ImageFileRepository imageFileRepository) {
        this.imageFileRepository = imageFileRepository;
    }

    public String saveImage(MultipartFile file) {
        ImageFile imageFile = null;
        // get name of the file
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        // get data of the file
        try {
            ImageData imData = new ImageData(file.getBytes());
            imageFile = new ImageFile(fileName, file.getContentType(), imData);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        // save image to the db
        imageFileRepository.save(imageFile);

        return imageFile.getId();

    }

    public ImageFile getImageById(String id) {
        ImageFile imageFile = imageFileRepository.findById(id).orElseThrow(() ->
                new ResourceNotFoundException(String.format(ErrorMessages.IMAGE_NOT_FOUND_EXCEPTION, id)));
        return imageFile;
    }

    public List<ImageFileDTO> getAllImages() {
        List<ImageFile> imageFiles = imageFileRepository.findAll();

        List<ImageFileDTO> imageFileDTOS = imageFiles.stream().map(imFile -> {
            // get url
            String imageUri = ServletUriComponentsBuilder
                    .fromCurrentContextPath() //localhost:8080
                    .path("/files/download/") //localhost:8080/files/download
                    .path(imFile.getId()).toUriString(); //localhost:8080/files/download/id
            return new ImageFileDTO(imFile.getName(),imageUri,imFile.getType(),imFile.getLength());
        }).collect(Collectors.toList());

        return imageFileDTOS;

    }

    public void deleteImageFileById(String id) {
        ImageFile imageFile = getImageById(id);
        imageFileRepository.delete(imageFile);
    }

    public ImageFile findImageById(String imageId) {
        return imageFileRepository.findImageById(imageId).orElseThrow(()->
                new ResourceNotFoundException(String.format(ErrorMessages.IMAGE_NOT_FOUND_EXCEPTION,imageId)));
    }
}
