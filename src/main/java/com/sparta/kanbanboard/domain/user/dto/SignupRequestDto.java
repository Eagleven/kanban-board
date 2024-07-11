package com.sparta.kanbanboard.domain.user.dto;

import com.sparta.kanbanboard.domain.user.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class SignupRequestDto {

    @NotBlank(message = "Required Username")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9])[a-z0-9]{10,20}$", message = "사용자 ID는 최소 10글자 이상, 최대 20글자 이하여야 합니다.")
    private String username;  // id for log in
    @NotBlank(message = "Required Password")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[!@#$%^&*()]).{10,}$", message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함합니다. \n비밀번호는 최소 10글자 이상이어야 합니다.")
    private String password;
    @NotBlank(message = "Required Name")
    @Pattern(regexp = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$", message = "이름은 영문 대소문자, 공백, 하이픈, 아포스트로피, 쉼표, 점만 포함할 수 있습니다.")
    private String name;
    @Email
    private String email;
}
