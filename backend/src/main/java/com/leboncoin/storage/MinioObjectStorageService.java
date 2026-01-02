package com.leboncoin.storage;

import com.leboncoin.config.MinioProperties;
import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.http.Method;
import jakarta.annotation.PostConstruct;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

@Service
public class MinioObjectStorageService implements ObjectStorageService {

    private static final Set<String> ALLOWED_CONTENT_TYPES = Set.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            "image/webp",
            "image/heif",
            "image/heic"
    );

    private static final DateTimeFormatter FOLDER_FORMATTER = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private final MinioClient minioClient;
    private final MinioProperties properties;

    public MinioObjectStorageService(MinioClient minioClient, MinioProperties properties) {
        this.minioClient = minioClient;
        this.properties = properties;
    }

    @PostConstruct
    void ensureBucket() {
        try {
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder()
                    .bucket(properties.getBucket())
                    .build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder()
                        .bucket(properties.getBucket())
                        .build());
            }
        } catch (Exception e) {
            throw new StorageException("Unable to verify/create MinIO bucket", e);
        }
    }

    @Override
    public String uploadImage(MultipartFile file) {
        validateFile(file);
        String objectName = buildObjectName(file);

        try (InputStream inputStream = file.getInputStream()) {
            PutObjectArgs args = PutObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .stream(inputStream, file.getSize(), -1)
                    .contentType(resolveContentType(file))
                    .build();

            minioClient.putObject(args);
            return objectName;
        } catch (Exception e) {
            throw new StorageException("Unable to upload image to MinIO", e);
        }
    }

    @Override
    public List<String> uploadImages(List<MultipartFile> files) {
        if (files == null || files.isEmpty()) {
            return List.of();
        }
        List<String> objectNames = new ArrayList<>(files.size());
        for (MultipartFile file : files) {
            objectNames.add(uploadImage(file));
        }
        return objectNames;
    }

    @Override
    public void deleteObject(String objectName) {
        if (!StringUtils.hasText(objectName)) {
            return;
        }
        try {
            minioClient.removeObject(RemoveObjectArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .build());
        } catch (Exception e) {
            throw new StorageException("Unable to delete object %s".formatted(objectName), e);
        }
    }

    @Override
    public void deleteObjects(Collection<String> objectNames) {
        if (objectNames == null || objectNames.isEmpty()) {
            return;
        }
        objectNames.forEach(this::deleteObject);
    }

    @Override
    public String generatePresignedGetUrl(String objectName) {
        if (!StringUtils.hasText(objectName)) {
            return null;
        }
        try {
            int expirySeconds = Math.toIntExact(clampPresignedExpiry(properties.getPresignedUrlDuration()));
            String presignedUrl = minioClient.getPresignedObjectUrl(GetPresignedObjectUrlArgs.builder()
                    .bucket(properties.getBucket())
                    .object(objectName)
                    .method(Method.GET)
                    .expiry(expirySeconds)
                    .build());

            String internalEndpoint = properties.getEndpoint();
            String externalEndpoint = properties.getExternalEndpoint();
            if (!internalEndpoint.equals(externalEndpoint)) {
                presignedUrl = presignedUrl.replace(internalEndpoint, externalEndpoint);
            }

            return presignedUrl;
        } catch (Exception e) {
            throw new StorageException("Unable to generate presigned URL for object %s".formatted(objectName), e);
        }
    }

    @Override
    public List<String> generatePresignedGetUrls(List<String> objectNames) {
        if (objectNames == null || objectNames.isEmpty()) {
            return List.of();
        }
        List<String> urls = new ArrayList<>(objectNames.size());
        for (String objectName : objectNames) {
            urls.add(generatePresignedGetUrl(objectName));
        }
        return urls;
    }

    private void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new StorageException("Empty file cannot be uploaded");
        }
        String contentType = resolveContentType(file);
        if (!ALLOWED_CONTENT_TYPES.contains(contentType)) {
            throw new StorageException("Unsupported content type: " + contentType);
        }
    }

    private String resolveContentType(MultipartFile file) {
        String contentType = file.getContentType();
        if (!StringUtils.hasText(contentType)) {
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
        String normalized = contentType.toLowerCase(Locale.ROOT);
        if (ALLOWED_CONTENT_TYPES.contains(normalized)) {
            return normalized;
        }
        return normalized;
    }

    private String buildObjectName(MultipartFile file) {
        String folder = FOLDER_FORMATTER.format(LocalDate.now());
        String extension = extractExtension(file.getOriginalFilename());
        return "ads/%s/%s%s".formatted(folder, UUID.randomUUID(), extension);
    }

    private String extractExtension(String filename) {
        if (!StringUtils.hasText(filename)) {
            return "";
        }
        String cleanedFilename = filename.strip();
        int dotIndex = cleanedFilename.lastIndexOf('.');
        if (dotIndex == -1) {
            return "";
        }
        String ext = cleanedFilename.substring(dotIndex);
        if (!ext.matches("\\.[A-Za-z0-9]{1,6}")) {
            return "";
        }
        return ext.toLowerCase(Locale.ROOT);
    }

    private long clampPresignedExpiry(Duration duration) {
        Duration fallback = Duration.ofHours(1);
        Duration effective = duration != null ? duration : fallback;
        long seconds = effective.toSeconds();
        long min = Duration.ofMinutes(1).toSeconds();
        long max = Duration.ofDays(7).toSeconds();
        if (seconds < min) {
            return min;
        }
        if (seconds > max) {
            return max;
        }
        return seconds;
    }
}
