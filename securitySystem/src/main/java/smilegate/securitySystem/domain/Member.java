package smilegate.securitySystem.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter @Setter @Entity
@ToString(of={"id","name","userId","email","password","phoneNumber"})
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String name;
    private String userId;
    private String email;
    private String password;
    private String phoneNumber;

    public Member() {

    }

    public Member(String name, String userId, String email, String password, String phoneNumber) {
        this.name = name;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
