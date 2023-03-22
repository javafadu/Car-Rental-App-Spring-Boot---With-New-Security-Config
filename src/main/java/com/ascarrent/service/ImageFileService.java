package com.ascarrent.service;

import com.ascarrent.repository.ImageFileRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageFileService {

    private final ImageFileRepository imageFileRepository;

    public ImageFileService(ImageFileRepository imageFileRepository) {
        this.imageFileRepository = imageFileRepository;
    }
}
