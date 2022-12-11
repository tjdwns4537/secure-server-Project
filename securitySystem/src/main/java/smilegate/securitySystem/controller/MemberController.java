package smilegate.securitySystem.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import smilegate.securitySystem.domain.Member;
import smilegate.securitySystem.repository.MemberRepositoryImp;
import smilegate.securitySystem.repository.MemberRepositoryInterface;
import smilegate.securitySystem.service.EmailService.EmailServiceImp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

    EmailServiceImp emailService = new EmailServiceImp();

    Map<String, String> error = new HashMap<>();
    MemberRepositoryInterface memberRepository = MemberRepositoryImp.getInstance();

    @GetMapping("/join")
    public String joinPage() {
        return "/member/join";
    }

    @PostMapping("/join")
    public String joinExecute(@RequestParam String password2, Member member, RedirectAttributes redirectAttributes, Model model) {
        inputErrorCheck(member.getName(), member.getPassword(), member.getPhoneNumber(), password2, member.getEmail());
        if(hasError()){
            log.info("log info:{}", error);
            model.addAttribute("error", error);
            return "/member/join";
        }
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

    @PostMapping("/emailConfirm")
    @ResponseBody
    public void emailConfirm(String email) throws Exception {

        log.info("post email = {}", email);
        String confirm = emailService.sendSimpleMessage(email);
    }

    @PostMapping("/verifyCode")
    @ResponseBody
    public int verifyCode(String code) {
        log.info("Post Verify = {}", code);
        int result = 0;
        String password = emailService.getEmailPassword();
        if(password.equals(code)){
            result = 1;
        }
        return result;
    }


    public boolean hasError() {
        if(!error.isEmpty()) return true;
        return false;
    }

    public void inputErrorCheck(String name, String password1, String phoneNumber, String password2, String email) {
        nameErrorCheck(name);
        phoneNumberErrorCheck(phoneNumber);
        passwordErrorCheck(password1, password2);
        emailErrorCheck(email);
    }

    public void emailErrorCheck(String str) {
        if(!StringUtils.hasText(str)) error.put("globalError", "이메일이 비어있습니다.");
    }

    public void nameErrorCheck(String str) {
        if(!StringUtils.hasText(str)) error.put("globalError", "이름이 비어있습니다.");
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
