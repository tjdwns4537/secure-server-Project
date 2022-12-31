package smilegate.securitySystem.domain;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

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

    public Member(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public Member(String name, String userId, String email, String password, String phoneNumber) {
        this.name = name;
        this.userId = userId;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
    }
}
