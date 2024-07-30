package com.example.mutsideout_mju.util;

import com.amazonaws.services.s3.AmazonS3;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

/**
 * AWS S3 관련된 기능 구현
 * 업로드, 조회
 */
@Service
@RequiredArgsConstructor
public class S3Service {
    private final AmazonS3 s3client;
    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    public String getRoomImageLink(String link) {
        String imagePath = getImagePath(link);
        return imagePath != null ? generateFileUrl(imagePath) : null;
    }

    private String generateFileUrl(String key) {
        return s3client.getUrl(bucketName.trim(), key).toString();
    }

    private String getImagePath(String link) {
        String basePath = "rooms/";
        if (link.contains("google")) {
            return basePath + "googlemeet.svg";
        } else if (link.contains("zoom")) {
            return basePath + "zoom.svg";
        } else if (link.contains("discord")) {
            return basePath + "discord.svg";
        } else {
            return null;
        }
    }
}
