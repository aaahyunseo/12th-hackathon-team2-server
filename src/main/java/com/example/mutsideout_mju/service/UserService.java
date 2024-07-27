package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.authentication.PasswordHashEncryption;
import com.example.mutsideout_mju.dto.request.user.DeleteUserDto;
import com.example.mutsideout_mju.dto.request.user.UpdateUserDto;
import com.example.mutsideout_mju.dto.response.user.UserGradeResponseDto;
import com.example.mutsideout_mju.entity.Grade;
import com.example.mutsideout_mju.entity.SurveyOption;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.entity.UserSurvey;
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

    @Transactional
    public UserGradeResponseDto calculateUserGrade(User user) {
        List<UserSurvey> userSurveyList = userSurveyRepository.findByUserId(user.getId());

        long count = userSurveyList.stream()
                .filter(userSurvey -> isValidSurveyOption(userSurvey))
                .count();
        Grade grade = user.determineGrade(count);

        user.setUserGrade(grade);
        userRepository.save(user);

        return UserGradeResponseDto.of(user.getName(), grade);
    }

    public UserGradeResponseDto getUserGrade(User user) {
        return UserGradeResponseDto.from(user.getUserGrade());
    }

    public static boolean isValidSurveyOption(UserSurvey userSurvey) {
        Long questionNumber = userSurvey.getSurvey().getNumber();
        SurveyOption option = userSurvey.getSurveyOption();

        return (questionNumber >= 1 && questionNumber <= 3 && (option == SurveyOption.NORMAL || option == SurveyOption.YES))
                || (questionNumber >= 4 && questionNumber <= 6 && option == SurveyOption.YES);
    }

    //회원 탈퇴
    public void deleteUser(User user, DeleteUserDto deleteUserDto) {
        validatePassword(deleteUserDto.getPassword(), user.getPassword());
        userRepository.delete(user);
    }

    //회원 정보 수정
    @Transactional
    public void updateUser(User user, UpdateUserDto updateUserDto) {
        validatePassword(updateUserDto.getOriginPassword(), user.getPassword());
        if (updateUserDto.getNewName() != null && !updateUserDto.getNewName().isEmpty()) {
            user.setName(updateUserDto.getNewName());
        }
        if (updateUserDto.getNewPassword() != null && !updateUserDto.getNewPassword().isEmpty()) {
            user.setPassword(passwordHashEncryption.encrypt(updateUserDto.getNewPassword()));
        }
        userRepository.save(user);
    }

    //비밀번호 일치 여부 확인
    public void validatePassword(String plainPassword, String hashedPassword) {
        if (!passwordHashEncryption.matches(plainPassword, hashedPassword)) {
            throw new ForbiddenException(ErrorCode.NO_ACCESS, "비밀번호 정보가 일치하지 않습니다.");
        }
    }
}
