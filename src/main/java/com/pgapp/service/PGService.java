package com.pgapp.service;

import com.pgapp.entity.Owner;
import com.pgapp.entity.PG;
import com.pgapp.exception.ResourceNotFoundException;
import com.pgapp.repository.OwnerRepository;
import com.pgapp.repository.PGRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class PGService {

    private final PGRepository pgRepo;
    private final OwnerRepository ownerRepo;

    public PGService(PGRepository pgRepo, OwnerRepository ownerRepo) {
        this.pgRepo = pgRepo;
        this.ownerRepo = ownerRepo;
    }

    // ✅ Create PG with only basic details (no floors/rooms here)
    public PG createPG(Long ownerId, PG pg) {
        Owner owner = ownerRepo.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found with id " + ownerId));
        pg.setOwner(owner);
        return pgRepo.save(pg);
    }


    // ✅ Update PG basic info
    public Optional<PG> updatePG(Long id, PG updatedPG) {
        return pgRepo.findById(id).map(pg -> {
            pg.setName(updatedPG.getName());
            pg.setHouseNo(updatedPG.getHouseNo());
            pg.setArea(updatedPG.getArea());
            pg.setCity(updatedPG.getCity());
            pg.setState(updatedPG.getState());
            pg.setPincode(updatedPG.getPincode());
            pg.setAmenities(updatedPG.getAmenities());
            pg.setShortTermAllowed(updatedPG.isShortTermAllowed());
            return pgRepo.save(pg);
        });
    }

    // ✅ Delete PG (and images with it)
    public boolean deletePG(Long id) {
        return pgRepo.findById(id).map(pg -> {
            deleteAllImages(pg);
            pgRepo.delete(pg);
            return true;
        }).orElse(false);
    }


    // ✅ Get PG by ID
    public Optional<PG> getPGById(Long id) {
        return pgRepo.findById(id);
    }

    public List<PG> getPGsByOwnerId(Long ownerId) {
        return pgRepo.findByOwnerId(ownerId);
    }


    // ✅ Get all PGs
    public List<PG> getAllPGs() {
        return pgRepo.findAll();
    }

    // ------------------- IMAGE HANDLING ------------------- //

    // Add images to PG
    public PG addImages(Long pgId, List<MultipartFile> images) {
        PG pg = pgRepo.findById(pgId)
                .orElseThrow(() -> new ResourceNotFoundException("PG not found with id " + pgId));

        List<String> imagePaths = new ArrayList<>(pg.getImages());

        for (MultipartFile file : images) {
            try {
                String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
                Path path = Paths.get("uploads/pg-images/" + filename);

                Files.createDirectories(path.getParent());
                Files.write(path, file.getBytes());

                String fileUrl = "/uploads/pg-images/" + filename;
                imagePaths.add(fileUrl);
            } catch (Exception e) {
                throw new RuntimeException("Failed to store image: " + file.getOriginalFilename());
            }
        }

        pg.setImages(imagePaths);
        return pgRepo.save(pg);
    }


    // Remove single image
    public PG removeImage(Long pgId, String imageUrl) {
        PG pg = pgRepo.findById(pgId)
                .orElseThrow(() -> new ResourceNotFoundException("PG not found with id " + pgId));

        pg.getImages().remove(imageUrl);

        try {
            Path path = Paths.get(imageUrl.replace("/uploads", "uploads"));
            Files.deleteIfExists(path);
        } catch (Exception ignored) {}

        return pgRepo.save(pg);
    }

    // Delete all images of a PG
    private void deleteAllImages(PG pg) {
        for (String imageUrl : pg.getImages()) {
            try {
                Path path = Paths.get(imageUrl.replace("/uploads", "uploads"));
                Files.deleteIfExists(path);
            } catch (Exception ignored) {}
        }
    }
}

