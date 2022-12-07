package smilegate.securitySystem.domain;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Member {
    private Long id;
    private String name;
    private String userId;
    private String email;
    private String password;
    private String phoneNumber;

    public Member(String name, String userId, String email, String password, String phoneNumber) {
        this.name = name;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
