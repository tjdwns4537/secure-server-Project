package smilegate.securitySystem.service.MemberService;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Service
public class JoinFormValidate {

    Map<String, String> error = new HashMap<>();
    boolean emailCheckFlag;

    private JoinFormValidate() {

    }

    public Map<String, String> getError(){
        return this.error;
    }

    public void inputErrorCheck(String userId, String name, String password1, String phoneNumber, String password2) {
//        nameEmptyErrorCheck(name);
        nameRuleErrorCheck(name);
        phoneNumberErrorCheck(phoneNumber);
        passwordErrorCheck(password1, password2);
//        emailVerifyPassError();
//        userIdErrorCheck(userId);
    }

    //    public void userIdErrorCheck(String str) {
//        if(!StringUtils.hasText(str)) error.put("userIdError", "아이디를 입력해야합니다.");
//    }
    public void emailVerifyPassError(){
        if(!emailCheckFlag) error.put("emailVerifyError", "email 검증을 완료해야합니다.");
    }

//    public void nameEmptyErrorCheck(String str) {
//        if(!StringUtils.hasText(str)) error.put("nameError", "이름이 비어있습니다.");
//    }

    public void nameRuleErrorCheck(String str){
        if(StringUtils.containsWhitespace(str)) error.put("nameError", "이름에 공백이 들어있습니다.");
        if(str.length() > 12) error.put("nameError", "이름 길이가 12자가 넘습니다.");
    }

    public void phoneNumberErrorCheck(String str){
        if(str.contains("-")) error.put("phoneNumberError", "연락처에 하이폰이 들어가 있습니다.");
        if(str.length() != 11) error.put("phoneNumberError", "연락처 길이가 11자리가 아닙니다.");
    }

    public void passwordErrorCheck(String pw1, String pw2 ){
        if(isValidPassword(pw1)) error.put("passwordError", "비밀번호 형식이 맞지 않습니다.");
        if(passwordEqualError(pw1, pw2)) error.put("passwordError", "비밀번호와 재확인 비밀번호가 다릅니다.");
    }

    public boolean passwordEqualError(String pw1,String pw2) {
        log.info("비밀번호1 : {}", pw1);
        log.info("비밀번호2 : {}", pw2);
        if(!pw1.equals(pw2)) return true;
        return false;
    }

    public boolean isValidPassword(String password) {
        String rex = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$ %^&*-]).{8,}$";
        boolean result = Pattern.matches(rex, password);
        if(!result) return true;
        return false;
    }
}
