package com.example.mutsideout_mju.controller;

import com.example.mutsideout_mju.authentication.AuthenticatedUser;
import com.example.mutsideout_mju.dto.request.PaginationDto;
import com.example.mutsideout_mju.dto.request.diary.UpdateDiaryDto;
import com.example.mutsideout_mju.dto.request.diary.WriteDiaryDto;
import com.example.mutsideout_mju.dto.response.diary.DiaryListResponseData;
import com.example.mutsideout_mju.dto.response.diary.DiaryResponseData;
import com.example.mutsideout_mju.dto.response.ResponseDto;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.service.DiaryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequiredArgsConstructor    //생성자 주입
@RequestMapping("/diaries")
public class DiaryController {

    private final DiaryService diaryService;

    //감정일기 전체 목록 조회
    @GetMapping
    public ResponseEntity<ResponseDto<DiaryListResponseData>> getDiaryList(@AuthenticatedUser User user, @RequestParam(value = "page", defaultValue = "1") int page){
        PaginationDto paginationDto = new PaginationDto(page);
        DiaryListResponseData diaryListResponseData = diaryService.getDiaryList(user, paginationDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK,"감정일기 전체 목록을 조회하였습니다.", diaryListResponseData), HttpStatus.OK);
    }

    //감정일기 상세 조회
    @GetMapping("/{diaryId}")
    public ResponseEntity<ResponseDto<DiaryResponseData>> getDiary(@AuthenticatedUser User user, @PathVariable UUID diaryId){
        DiaryResponseData diaryResponseData = diaryService.getDiary(user, diaryId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK,"감정일기 전체 목록을 조회하였습니다.", diaryResponseData), HttpStatus.OK);
    }

    //감정일기 작성
    @PostMapping
    public ResponseEntity<ResponseDto<Void>> writeDiary(@AuthenticatedUser User user, @RequestBody @Valid WriteDiaryDto writeDiaryDto){
        diaryService.writeDiary(user, writeDiaryDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.CREATED,"감정일기가 정상적으로 작성되었습니다."), HttpStatus.CREATED);
    }

    //감정일기 수정
    @PatchMapping("/{diaryId}")
    public ResponseEntity<ResponseDto<Void>> updateDiary(@AuthenticatedUser User user, @PathVariable UUID diaryId,  @RequestBody @Valid UpdateDiaryDto updateDiaryDto){
        diaryService.updateDiary(user, diaryId, updateDiaryDto);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK,"감정일기가 정상적으로 수정되었습니다."), HttpStatus.OK);
    }

    //감정일기 삭제
    @DeleteMapping("/{diaryId}")
    public ResponseEntity<ResponseDto<Void>> deleteDiary(@AuthenticatedUser User user, @PathVariable UUID diaryId){
        diaryService.deleteDiary(user, diaryId);
        return new ResponseEntity<>(ResponseDto.res(HttpStatus.OK,"감정일기가 정상적으로 삭제되었습니다."), HttpStatus.OK);
    }
}
