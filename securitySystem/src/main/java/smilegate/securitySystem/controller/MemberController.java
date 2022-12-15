package smilegate.securitySystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import smilegate.securitySystem.domain.Member;
import smilegate.securitySystem.repository.MemberRepository.MemberRepositoryImp;
import smilegate.securitySystem.repository.MemberRepository.MemberRepositoryInterface;
import smilegate.securitySystem.service.EmailService.EmailServiceImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

    @Autowired
    EmailServiceImp emailService;

    Map<String, String> error = new HashMap<>();
    MemberRepositoryInterface memberRepository = MemberRepositoryImp.getInstance();

    String emailVerifyCode;
    boolean emailCheckFlag;
    String globalEmail;

    @GetMapping("/join")
    public String joinPage(Model model) {
        emailCheckFlag = false;
        model.addAttribute("member", new Member());
        return "/member/join";
    }

    @PostMapping("/join")
    public String joinExecute(
            @ModelAttribute Member member,
            @RequestParam String password2,
            RedirectAttributes redirectAttributes,
            Model model) {
        error.clear();
        inputErrorCheck(member.getUserId(), member.getName(), member.getPassword(), member.getPhoneNumber(), password2, member.getEmail());
        if(hasError()){
            log.info("log info:{}", error);
            model.addAttribute("error", error);
            return "/member/join";
        }
        member.setEmail(globalEmail);
        memberRepository.save(member);
        redirectAttributes.addAttribute("memberId", member.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/member/" + member.getId();
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/member/login";
    }

    @PostMapping("/login")
    public String loginExecute(Model model) {
        return "/index";
    }

    @GetMapping("/{memberId}")
    public String checkMemberPage(@PathVariable Long memberId, Model model) {
        Member member = memberRepository.findById(memberId);
        model.addAttribute("member", member);
        return "/member/checkMember";
    }

    @GetMapping("/memberList")
    public String memberViewPage(Model model) {
        List<Member> allMember = memberRepository.findAll();
        model.addAttribute("members", allMember);
        return "/member/memberListView";
    }

    @GetMapping("/emailConfirm")
    public String emailVerify(Model model) {
        model.addAttribute("member", new Member());
        return "/member/join";
    }

    @PostMapping("/emailConfirm")
    @ResponseBody
    public String emailVerify(@RequestParam String email, Model model) throws Exception {
        error.clear();
        emailErrorCheck(email);
        if(hasError()){
            log.info("log info:{}", error);
            model.addAttribute("error", error);
            return "/member/emailConfirm";
        }
        globalEmail = email;
        log.info("post email = {}", email);
        String s = emailService.sendSimpleMessage(email);
        emailVerifyCode = s;
        return "";
    }

    @PostMapping("/emailVerify")
    @ResponseBody
    public void verifyCode(@RequestParam String emailCheck) {
        log.info("Post Verify = {}", emailCheck);
        if(!emailVerifyCode.equals(emailCheck)){
            log.info("fail verify");
            emailCheckFlag = false;
            error.put("verifyError", "인증번호를 잘못 입력하셨습니다.");
        }
        if(emailVerifyCode.equals(emailCheck)){
            log.info("success verify");
            emailCheckFlag = true;
        }
    }

    public boolean hasError() {
        if(!error.isEmpty()) return true;
        return false;
    }

    public void inputErrorCheck(String userId, String name, String password1, String phoneNumber, String password2, String email) {
        nameEmptyErrorCheck(name);
        nameRuleErrorCheck(name);
        phoneNumberErrorCheck(phoneNumber);
        passwordErrorCheck(password1, password2);
        emailVerifyPassError();
        userIdErrorCheck(userId);
    }

    public void userIdErrorCheck(String str) {
        if(!StringUtils.hasText(str)) error.put("userIdError", "아이디를 입력해야합니다.");
    }
    public void emailVerifyPassError(){
        if(!emailCheckFlag) error.put("emailVerifyError", "email 검증을 완료해야합니다.");
    }

    public void emailErrorCheck(String str) {
        if(!StringUtils.hasText(str)) error.put("emailError", "이메일이 비어있습니다.");
    }

    public void nameEmptyErrorCheck(String str) {
        if(!StringUtils.hasText(str)) error.put("nameError", "이름이 비어있습니다.");
    }

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
