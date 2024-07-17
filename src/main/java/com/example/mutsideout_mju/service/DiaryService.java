package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.dto.request.diary.UpdateDiaryDto;
import com.example.mutsideout_mju.dto.request.diary.WriteDiaryDto;
import com.example.mutsideout_mju.dto.response.diary.DiaryListResponseData;
import com.example.mutsideout_mju.dto.response.diary.DiaryResponseData;
import com.example.mutsideout_mju.entity.Diary;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.exception.ForbiddenException;
import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.DiaryRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class DiaryService {

    private DiaryRepository diaryRepository;
    private final static int PAGE_SIZE = 10;    //10개씩 페이징

    //감정일기 전체 목록 조회
    public DiaryListResponseData getDiaryList(int page){
        int pageSize = PAGE_SIZE;  //한 페이지에 게시글 10개씩
        Sort sort = Sort.by(Sort.Order.desc("createdAt"));  //작성순으로 정렬
        Pageable pageable = PageRequest.of(page, pageSize, sort);   //페이지 번호, 페이지 크기, 정렬 조건 설정

        Page<Diary> diaryPage = diaryRepository.findAll(pageable);  //해당 페이징 데이터를 모두 가져옴

        if(diaryPage.getTotalPages() <= page && page!=0){
            throw new NotFoundException(ErrorCode.NOT_FOUND_PAGE);
        }

        return DiaryListResponseData.diaryListResponseData(diaryPage);
    }

    //감정일기 상세 조회
    public DiaryResponseData getDiary(User user, UUID diaryId){
        Diary diary = findDiaryById(diaryId);
        checkUser(user, diary);
        return DiaryResponseData.diaryResponseData(diary);
    }

    //감정일기 작성
    public void writeDiary(User user, WriteDiaryDto writeDiaryDto){
        Diary diary = Diary.builder()
                .user(user)
                .title(writeDiaryDto.getTitle())
                .content(writeDiaryDto.getContent())
                .build();
        diaryRepository.save(diary);
    }

    //감정일기 수정
    public void updateDiary(User user, UUID diaryId, UpdateDiaryDto updateDiaryDto){
        Diary newDiary = findDiaryById(diaryId);
        checkUser(user, newDiary);
        newDiary.setTitle(updateDiaryDto.getTitle())
                .setContent(updateDiaryDto.getContent());
        diaryRepository.save(newDiary);
    }

    //감정일기 삭제
    public void deleteDiary(User user, UUID diaryId){
        Diary diary = findDiaryById(diaryId);
        checkUser(user, diary);
        diaryRepository.deleteById(diaryId);
    }

    //감정일기 존재 여부 확인
    private Diary findDiaryById(UUID diaryId){
        return diaryRepository.findById(diaryId).orElseThrow(()-> new NotFoundException(ErrorCode.DIARY_NOT_FOUND));
    }

    //접근 유저와 감정일기 작성자가 일치한지 확인
    private void checkUser(User user, Diary diary){
        if(!diary.getUser().getId().equals(user.getId())){
            throw new ForbiddenException(ErrorCode.NO_ACCESS);
        }
    }
}
