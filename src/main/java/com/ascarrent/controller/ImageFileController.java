package com.ascarrent.controller;


import com.ascarrent.domain.ImageFile;
import com.ascarrent.dto.ImageFileDTO;
import com.ascarrent.dto.response.ACRResponse;
import com.ascarrent.dto.response.ImageSavedResponse;
import com.ascarrent.dto.response.ResponseMessage;
import com.ascarrent.service.ImageFileService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/files")
public class ImageFileController {

    private final ImageFileService imageFileService;

    public ImageFileController(ImageFileService imageFileService) {
        this.imageFileService = imageFileService;
    }

    // -- Upload Image
    // imageId: ca20d6da-96b4-40c4-8075-37af088d4e4a
    @PostMapping("/upload")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ImageSavedResponse> uploadFile(
            @RequestParam("file") MultipartFile file) {

        String imageId = imageFileService.saveImage(file);
        ImageSavedResponse response = new ImageSavedResponse(ResponseMessage.IMAGE_SAVE_RESPONSE_MESSAGE, true, imageId);

        return ResponseEntity.ok(response);
    }

    // -- Download Image
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String id) {
        ImageFile imageFile = imageFileService.getImageById(id);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + imageFile.getName())
                .body(imageFile.getImageData().getData());
    }

    // -- Image Display
    @GetMapping("/display/{id}")
    public ResponseEntity<byte[]> displayFile(@PathVariable String id) {
        ImageFile imageFile = imageFileService.getImageById(id);

        HttpHeaders header = new HttpHeaders();
        header.setContentType(MediaType.IMAGE_PNG);

        return new ResponseEntity<>(
                imageFile.getImageData().getData(),
                header,
                HttpStatus.OK);
    }

    // -- Get All Images
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ImageFileDTO>> getAllImages() {
        List<ImageFileDTO> allImageDTO = imageFileService.getAllImages();
        return ResponseEntity.ok(allImageDTO);
    }

    // -- Delete Image With Id
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ACRResponse> deleteImageFile(@PathVariable String id) {
        imageFileService.deleteImageFileById(id);
        ACRResponse response = new ACRResponse(ResponseMessage.IMAGE_DELETE_RESPONSE_MESSAGE,true);
        return ResponseEntity.ok(response);
    }


}
