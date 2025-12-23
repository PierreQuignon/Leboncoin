package com.leboncoin.controller;

import com.leboncoin.dto.ImageUploadResponseDTO;
import com.leboncoin.storage.ObjectStorageService;
import com.leboncoin.storage.StorageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/ads/images")
public class AdImageController {

    private final ObjectStorageService objectStorageService;

    public AdImageController(ObjectStorageService objectStorageService) {
        this.objectStorageService = objectStorageService;
    }

    @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponseDTO> uploadImages(
            @RequestParam("files") List<MultipartFile> files,
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (CollectionUtils.isEmpty(files)) {
            throw new StorageException("No files provided");
        }

        ImageUploadResponseDTO response = new ImageUploadResponseDTO();
        List<String> objectNames = objectStorageService.uploadImages(files);
        objectNames.forEach(objectName -> response.addImage(new ImageUploadResponseDTO.UploadedImageDTO(
                objectName,
                objectStorageService.generatePresignedGetUrl(objectName)
        )));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
