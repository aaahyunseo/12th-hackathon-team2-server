package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.authentication.PasswordHashEncryption;
import com.example.mutsideout_mju.dto.request.user.DeleteUserDto;
import com.example.mutsideout_mju.dto.request.user.UpdateUserDto;
import com.example.mutsideout_mju.dto.response.user.ProfileResponseData;
import com.example.mutsideout_mju.dto.response.user.UserGradeResponseDto;
import com.example.mutsideout_mju.entity.Grade;
import com.example.mutsideout_mju.entity.SurveyOption;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.entity.UserSurvey;
import com.example.mutsideout_mju.exception.ConflictException;
import com.example.mutsideout_mju.exception.ForbiddenException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.UserRepository;
import com.example.mutsideout_mju.repository.UserSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserSurveyRepository userSurveyRepository;
    private final PasswordHashEncryption passwordHashEncryption;

    /**
     * 유저 등급 계산
     */
    @Transactional
    public UserGradeResponseDto calculateUserGrade(User user) {
        List<UserSurvey> userSurveyList = userSurveyRepository.findByUserId(user.getId());

        long count = userSurveyList.stream()
                .filter(userSurvey -> isValidSurveyOption(userSurvey))
                .count();
        Grade grade = user.determineGrade(count);

        user.setGrade(grade);
        userRepository.save(user);

        return UserGradeResponseDto.of(user.getName(), grade);
    }

    /**
     * 유저 등급 반환
     */
    public UserGradeResponseDto getUserGrade(User user) {
        return UserGradeResponseDto.from(user.getGrade());
    }

    /**
     * 설문조사 응답 유효 검사
     */
    public static boolean isValidSurveyOption(UserSurvey userSurvey) {
        Long questionNumber = userSurvey.getSurvey().getNumber();
        SurveyOption option = userSurvey.getSurveyOption();

        return (questionNumber >= 1 && questionNumber <= 3 && (option == SurveyOption.NORMAL || option == SurveyOption.YES))
                || (questionNumber >= 4 && questionNumber <= 6 && option == SurveyOption.YES);
    }

    /**
     * 유저 탈퇴
     */
    public void deleteUser(User user, DeleteUserDto deleteUserDto) {
        validatePassword(deleteUserDto.getPassword(), user.getPassword());
        userRepository.delete(user);
    }

    /**
     * 유저 정보 수정
     */
    @Transactional
    public void updateUser(User user, UpdateUserDto updateUserDto) {
        validatePassword(updateUserDto.getOriginPassword(), user.getPassword());
        if (updateUserDto.getNewName() != null && !updateUserDto.getNewName().isEmpty()) {
            //중복된 이름이 있을 경우
            if (userRepository.findByName(updateUserDto.getNewName()).isPresent()) {
                throw new ConflictException(ErrorCode.DUPLICATED_NAME);
            }
            user.setName(updateUserDto.getNewName());
        }
        if (updateUserDto.getNewPassword() != null && !updateUserDto.getNewPassword().isEmpty()) {
            user.setPassword(passwordHashEncryption.encrypt(updateUserDto.getNewPassword()));
        }
        userRepository.save(user);
    }

    /**
     * 유저 전체 정보(이메일, 이름, 등급) 반환
     */
    public ProfileResponseData getMyPage(User user){
        ProfileResponseData profileResponseData = ProfileResponseData.of(user.getEmail(), user.getName(), user.getGrade());
        return profileResponseData;
    }

    /**
     * 비밀번호 일치 여부 확인
     */
    public void validatePassword(String plainPassword, String hashedPassword) {
        if (!passwordHashEncryption.matches(plainPassword, hashedPassword)) {
            throw new ForbiddenException(ErrorCode.NO_ACCESS, "비밀번호 정보가 일치하지 않습니다.");
        }
    }
}
