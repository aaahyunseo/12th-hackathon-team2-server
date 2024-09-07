package com.example.mutsideout_mju.service;

import com.example.mutsideout_mju.authentication.PasswordHashEncryption;
import com.example.mutsideout_mju.dto.request.user.DeleteUserDto;
import com.example.mutsideout_mju.dto.request.user.UpdateUserDto;
import com.example.mutsideout_mju.dto.response.user.UserInfoResponseDto;
import com.example.mutsideout_mju.entity.RefreshToken;
import com.example.mutsideout_mju.entity.UserGrade;
import com.example.mutsideout_mju.entity.User;
import com.example.mutsideout_mju.exception.ConflictException;
import com.example.mutsideout_mju.exception.UnauthorizedException;
import com.example.mutsideout_mju.exception.NotFoundException;
import com.example.mutsideout_mju.exception.errorCode.ErrorCode;
import com.example.mutsideout_mju.repository.UserRepository;
import com.example.mutsideout_mju.repository.usersurvey.UserSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserSurveyRepository userSurveyRepository;
    private final PasswordHashEncryption passwordHashEncryption;

    /**
     * 설문조사 유효 응답갯수로 유저 등급 계산
     */
    @Transactional
    public UserInfoResponseDto calculateUserGrade(User user) {
        long count = userSurveyRepository.countValidSurveyResponse(user.getId());
        UserGrade userGrade = user.determineGrade(count);

        user.setUserGrade(userGrade);
        userRepository.save(user);

        return UserInfoResponseDto.of(user.getName(), userGrade);
    }

    /**
     * 유저 등급 반환
     */
    public UserInfoResponseDto getUserGrade(User user) {
        return UserInfoResponseDto.from(user.getUserGrade());
    }

    /**
     * 유저 탈퇴
     */
    public void deleteUser(User user, DeleteUserDto deleteUserDto) {
        validateIsPasswordMatches(deleteUserDto.getPassword(), user.getPassword());
        userRepository.delete(user);
    }

    /**
     * 유저 정보 수정
     */
    @Transactional
    public void updateUser(User user, UpdateUserDto updateUserDto) {
        validateIsPasswordMatches(updateUserDto.getOriginPassword(), user.getPassword());
        if (updateUserDto.getNewName() != null && !updateUserDto.getNewName().isEmpty()) {
            //중복된 이름이 있을 경우
            if (userRepository.existsByEmail(updateUserDto.getNewName())) {
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
     * 유저 전체 정보(이메일, 이름, 등급) 조회
     */
    public UserInfoResponseDto getMyPage(User user) {
        return UserInfoResponseDto.of(user.getEmail(), user.getName(), user.getUserGrade());
    }

    public void validateIsPasswordMatches(String requestedPassword, String userPassword) {
        if (!passwordHashEncryption.matches(requestedPassword, userPassword)) {
            throw new UnauthorizedException(ErrorCode.INVALID_EMAIL_OR_PASSWORD);
        }
    }

    public void validateIsDuplicatedEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ConflictException(ErrorCode.DUPLICATED_EMAIL);
        }
    }

    public void validateIsDuplicatedName(String name) {
        if (userRepository.existsByName(name)) {
            throw new ConflictException(ErrorCode.DUPLICATED_NAME);
        }
    }
    public User findExistingUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException(ErrorCode.INVALID_EMAIL_OR_PASSWORD));
    }
    public User findExistingUserByRefreshToken(RefreshToken refreshToken) {
        return userRepository.findById(refreshToken.getUserId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}
