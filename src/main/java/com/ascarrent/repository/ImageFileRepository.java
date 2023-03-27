package com.ascarrent.repository;

import com.ascarrent.domain.ImageFile;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ImageFileRepository extends JpaRepository<ImageFile, String> {

    // prevent bringing imageData to get less data
    @EntityGraph(attributePaths = "id") // brings id level instances (not sub instances)
    List<ImageFile> findAll();

    @EntityGraph(attributePaths = "id") // brings id level instances (not sub instances)
    Optional<ImageFile> findImageById(String id);
}
