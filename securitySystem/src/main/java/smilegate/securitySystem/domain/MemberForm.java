package smilegate.securitySystem.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {

    @NotEmpty(message = "회원 이름은 필수정보입니다.")
    private String name;

    @NotEmpty(message = "이메일 정보가 비어있습니다.")
    private String email;

    @NotEmpty(message =  "이메일 인증번호가 비어있습니다.")
    private String emailCheck;

    @NotEmpty(message =  "비밀번호는 필수정보입니다.")
    private String password;

    private String password2;

    @NotEmpty(message = "아이디는 필수정보입니다.")
    private String userId;

    @NotEmpty(message = "휴대폰 번호는 필수정보입니다.")
    private String phoneNumber;
}
