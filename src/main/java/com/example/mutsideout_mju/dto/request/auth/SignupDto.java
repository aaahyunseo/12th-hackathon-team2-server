package com.example.mutsideout_mju.dto.request.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;


@Getter
@AllArgsConstructor
public class SignupDto {
    @NotNull(message = "사용자 이메일이 비어있습니다.")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{1,30}$", message = "이메일이 형식에 맞지 않습니다.")
    @NotBlank(message = "사용하실 이메일을 입력해주세요.")
    @Size(min = 1, max = 30, message = "이메일은 최소 한글자 최대 30글자 입니다")
    private String email;

    @NotNull(message = "사용자 패스워드가 비어있습니다.")
    @NotBlank(message = "영문과 숫자,특수기호를 조합하여 8~20글자 미만으로 입력하여 주세요.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[!@#$%^&*()])[A-Za-z\\d!@#$%^&*()]{8,20}$", message = "영문과 숫자,특수기호를 조합하여 8~20글자 미만으로 입력하여 주세요.")
    @Size(min = 8, max = 20, message = " 8 ~ 20글자 미만으로 입력하여 주세요.")
    private String password;

    @NotNull(message = "사용자 이름이 비어있습니다.")
    @NotBlank(message = "사용하실 이름을 입력해주세요.")
    @Size(min = 1, max = 10, message = "이름은 최소 한글자 최대 10글자 입니다")
    private String name;
}
