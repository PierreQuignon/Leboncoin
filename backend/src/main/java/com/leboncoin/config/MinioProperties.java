package com.leboncoin.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@ConfigurationProperties(prefix = "minio")
public class MinioProperties {

    private String endpoint;
    private String externalEndpoint;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private boolean secure;
    private Duration presignedUrlDuration = Duration.ofHours(1);

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getExternalEndpoint() {
        return externalEndpoint != null ? externalEndpoint : endpoint;
    }

    public void setExternalEndpoint(String externalEndpoint) {
        this.externalEndpoint = externalEndpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public Duration getPresignedUrlDuration() {
        return presignedUrlDuration;
    }

    public void setPresignedUrlDuration(Duration presignedUrlDuration) {
        this.presignedUrlDuration = presignedUrlDuration;
    }
}
