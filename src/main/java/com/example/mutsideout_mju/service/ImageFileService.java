package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.entity.Diary;
import com.example.mutsideout_mju.entity.ImageFile;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.repository.ImageFileRepository;
import com.example.mutsideout_mju.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageFileService {
    private final ImageFileRepository imageFileRepository;
    private final S3Service s3Service;

    /**
     * 이미지 업로드
     */
    @Transactional
    public void uploadImages(User user, Diary diary, List<MultipartFile> images) throws IOException {
        List<String> imageUrls = s3Service.uploadImage(images);

        for (String imageUrl : imageUrls) {
            ImageFile newImage = ImageFile.builder()
                    .imageUrl(imageUrl)
                    .diary(diary)
                    .user(user)
                    .build();
            imageFileRepository.save(newImage);
        }
    }

    /**
     * 이미지 삭제
     */
    @Transactional
    public void deleteImages(Diary diary) {
        List<ImageFile> filesToDelete = new ArrayList<>(diary.getImageFiles());
        if (!filesToDelete.isEmpty()) {
            filesToDelete.forEach(img -> s3Service.deleteImage(img.getImageUrl()));
            imageFileRepository.deleteAll(filesToDelete);
            diary.getImageFiles().clear();
        }
    }
}
