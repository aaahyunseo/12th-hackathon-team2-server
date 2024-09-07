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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DiaryService {

    private final DiaryRepository diaryRepository;
    private final ImageFileService imageService;

    /**
     * 감정일기 전체 목록 조회
     */
    public DiaryListResponseData getDiaryList(User user, PaginationDto paginationDto) {

        int page = paginationDto.getPage();
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));

        //요청받은 페이지 번호, 페이지 크기, 작성순으로 정렬
        Pageable pageable = PageRequest.of(page, paginationDto.getPageSize(), sort);

        //특정 유저의 해당 페이지 데이터를 모두 가져옴
        Page<Diary> diaryPage = diaryRepository.findByUserId(user.getId(), pageable);

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
    @Transactional
    public void writeDiary(User user, WriteDiaryDto writeDiaryDto, List<MultipartFile> images) throws IOException {
        Diary diary = new Diary(user,writeDiaryDto.getTitle(),writeDiaryDto.getContent());
        diary = diaryRepository.save(diary);
        uploadImages(diary, images);
    }

    /**
     * 감정일기 수정
     */
    @Transactional
    public void updateDiaryById(User user, UUID diaryId,
                                UpdateDiaryDto updateDiaryDto,
                                List<MultipartFile> images,
                                List<UUID> imageIdsToDelete) throws IOException {
        Diary newDiary = findDiary(user.getId(), diaryId);
        // 이미지 삭제
        if (imageIdsToDelete != null && !imageIdsToDelete.isEmpty()) {
            imageService.deleteImagesByImageIds(imageIdsToDelete);
        }

        uploadImages(newDiary, images);

        newDiary.setTitle(updateDiaryDto.getTitle())
                .setContent(updateDiaryDto.getContent());
        diaryRepository.save(newDiary);
    }

    /**
     * 감정일기 삭제
     */
    @Transactional
    public void deleteDiaryById(User user, UUID diaryId) {
        Diary diary = findDiary(user.getId(), diaryId);
        imageService.deleteImages(diary);
        diaryRepository.delete(diary);
    }


    /**
     * 감정일기 이미지 파일 업로드
     */
    public void uploadImages(Diary diary, List<MultipartFile> images) throws IOException {
        if (images != null && !images.isEmpty()) {
            imageService.uploadImages(diary.getUser(), diary, images);
        }
    }
}
