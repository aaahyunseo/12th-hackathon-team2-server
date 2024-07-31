package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.request.PaginationDto;
import com.example.mutsideout_mju.dto.request.diary.UpdateDiaryDto;
import com.example.mutsideout_mju.dto.request.diary.WriteDiaryDto;
import com.example.mutsideout_mju.dto.response.diary.DiaryListResponseData;
import com.example.mutsideout_mju.dto.response.diary.DiaryResponseData;
import com.example.mutsideout_mju.entity.Diary;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.DiaryRepository;
import com.example.mutsideout_mju.util.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final S3Service s3Service;

    /**
     * 감정일기 전체 목록 조회
     */
    public DiaryListResponseData getDiaryList(User user, PaginationDto paginationDto) {

        //요청받은 페이지 번호, 페이지 크기, 작성순으로 정렬
        Pageable pageable = PageRequest.of(paginationDto.getPage(), paginationDto.getPAGE_SIZE(), Sort.by(Sort.Order.desc("createdAt")));

        //특정 유저의 해당 페이지 데이터를 모두 가져옴
        Page<Diary> diaryPage = diaryRepository.findByUserId(user.getId(), pageable);

        if (diaryPage.getTotalPages() <= paginationDto.getPage() && paginationDto.getPage() != 0) {
            throw new NotFoundException(ErrorCode.NOT_FOUND_PAGE);
        }

        return DiaryListResponseData.from(diaryPage);
    }

    /**
     * 감정일기 상세 조회
     */
    public DiaryResponseData getDiaryById(User user, UUID diaryId) {
        Diary diary = findDiary(user.getId(), diaryId);
        return DiaryResponseData.from(diary);
    }

    private Diary findDiary(UUID userId, UUID diaryId) {
        return diaryRepository.findByUserIdAndId(userId, diaryId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.DIARY_NOT_FOUND));
    }

    /**
     * 감정일기 생성
     */
    public void writeDiary(User user, WriteDiaryDto writeDiaryDto, MultipartFile image) throws IOException {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = s3Service.uploadImage(image);
        }

        Diary diary = Diary.builder()
                .user(user)
                .title(writeDiaryDto.getTitle())
                .content(writeDiaryDto.getContent())
                .imageUrl(imageUrl)
                .build();
        diaryRepository.save(diary);
    }

    /**
     * 감정일기 수정
     */
    public void updateDiaryById(User user, UUID diaryId, UpdateDiaryDto updateDiaryDto, MultipartFile image) throws IOException {
        Diary newDiary = findDiary(user.getId(), diaryId);

        if (image != null && !image.isEmpty()) {
            //기존 이미지 삭제
            if (newDiary.getImageUrl() != null) {
                s3Service.deleteImage(newDiary.getImageUrl());
            }
            //새로운 이미지 업로드
            newDiary.setImageUrl(s3Service.uploadImage(image));
        }
        newDiary.setTitle(updateDiaryDto.getTitle())
                .setContent(updateDiaryDto.getContent());
        diaryRepository.save(newDiary);
    }

    /**
     * 감정일기 삭제
     */
    public void deleteDiaryById(User user, UUID diaryId) {
        Diary diary = findDiary(user.getId(), diaryId);
        if (diary.getImageUrl() != null) {
            s3Service.deleteImage(diary.getImageUrl());
        }
        diaryRepository.delete(diary);
    }
}
