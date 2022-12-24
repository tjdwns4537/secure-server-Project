package smilegate.securitySystem.domain;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter
@Setter
public class EmailForm {
    @NotEmpty(message = "이메일 정보가 비어있습니다.")
    private String email;

    @NotEmpty(message =  "이메일 인증번호가 비어있습니다.")
    private String emailCheck;
}
