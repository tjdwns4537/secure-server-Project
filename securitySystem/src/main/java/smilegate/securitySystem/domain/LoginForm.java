package smilegate.securitySystem.domain;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginForm {
    private String userId;
    private String password;
}
