package com.leboncoin.dto;

import java.util.ArrayList;
import java.util.List;

public class ImageUploadResponseDTO {

    private List<UploadedImageDTO> images = new ArrayList<>();

    public List<UploadedImageDTO> getImages() {
        return images;
    }

    public void setImages(List<UploadedImageDTO> images) {
        this.images = images;
    }

    public void addImage(UploadedImageDTO dto) {
        if (images == null) {
            images = new ArrayList<>();
        }
        images.add(dto);
    }

    public static class UploadedImageDTO {

        private String objectName;
        private String previewUrl;

        public UploadedImageDTO() {
        }

        public UploadedImageDTO(String objectName, String previewUrl) {
            this.objectName = objectName;
            this.previewUrl = previewUrl;
        }

        public String getObjectName() {
            return objectName;
        }

        public void setObjectName(String objectName) {
            this.objectName = objectName;
        }

        public String getPreviewUrl() {
            return previewUrl;
        }

        public void setPreviewUrl(String previewUrl) {
            this.previewUrl = previewUrl;
        }
    }
}
