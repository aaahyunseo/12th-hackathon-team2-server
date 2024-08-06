package com.example.mutsideout_mju.util;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * AWS S3 관련된 기능 구현
 * 업로드, 조회, 삭제
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

    public List<String> uploadImage(List<MultipartFile> files) throws IOException {
        List<String> imageUrls = new ArrayList<>();

        for (MultipartFile file : files) {
            String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
            String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

            String key = "diaries/" + UUID.randomUUID().toString() + "-" + dateTime + extension;
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(file.getSize());
            metadata.setContentType(file.getContentType());
            s3client.putObject(bucketName, key, file.getInputStream(), metadata);

            String imageUrl = generateFileUrl(key);
            imageUrls.add(imageUrl);
        }
        return imageUrls;
    }

    public void deleteImage(String imageUrl) {
        String splitStr = ".com/";
        String key = imageUrl.substring(imageUrl.lastIndexOf(splitStr) + splitStr.length());
        s3client.deleteObject(new DeleteObjectRequest(bucketName, key));
    }
}