package com.leboncoin.storage;

import org.springframework.web.multipart.MultipartFile;

import java.util.Collection;
import java.util.List;

public interface ObjectStorageService {

    String uploadImage(MultipartFile file);

    List<String> uploadImages(List<MultipartFile> files);

    void deleteObject(String objectName);

    void deleteObjects(Collection<String> objectNames);

    String generatePresignedGetUrl(String objectName);

    List<String> generatePresignedGetUrls(List<String> objectNames);
}
