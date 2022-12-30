package smilegate.securitySystem.controller;

import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import smilegate.securitySystem.domain.EmailForm;
import smilegate.securitySystem.domain.LoginForm;
import smilegate.securitySystem.domain.Member;
import smilegate.securitySystem.domain.MemberForm;
import smilegate.securitySystem.dto.TokenDto;
import smilegate.securitySystem.repository.MemberRepository.MemberRepositoryImp;
import smilegate.securitySystem.repository.MemberRepository.MemberRepositoryInterface;
import smilegate.securitySystem.service.EmailService.EmailServiceImp;
import smilegate.securitySystem.service.MemberService.MemberServiceInterface;
import smilegate.securitySystem.service.SecurityService.JwtFilter;
import smilegate.securitySystem.service.SecurityService.TokenProvider;
import springfox.documentation.spring.web.json.Json;

import javax.validation.Valid;
import java.time.Duration;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Slf4j
@Controller
@RequestMapping("/member")
public class MemberController {

    private final TokenProvider tokenProvider;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final EmailServiceImp emailService;
    private final MemberServiceInterface memberService;

    public MemberController(
            TokenProvider tokenProvider,
            AuthenticationManagerBuilder authenticationManagerBuilder,
            EmailServiceImp emailService,
            MemberServiceInterface memberService) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.emailService = emailService;
        this.memberService = memberService;
    }

    Map<String, String> error = new HashMap<>();
    String emailVerifyCode;
    boolean emailCheckFlag;
    String globalEmail;

    @GetMapping("/join")
    public String joinPage(Model model) {
        emailCheckFlag = false;
        model.addAttribute("memberForm", new MemberForm());
        return "/member/join";
    }

    @PostMapping("/join")
    public String joinExecute(
            @Valid MemberForm memberForm,BindingResult bindingResult,
            @RequestParam String password2,
            RedirectAttributes redirectAttributes,
            Model model) {

        if(bindingResult.hasErrors()){
            return "/member/join";
        }

        error.clear();
        inputErrorCheck(memberForm.getUserId(), memberForm.getName(), memberForm.getPassword(), memberForm.getPhoneNumber(), password2);

//        if(hasError()){
//            log.info("log info:{}", error);
//            model.addAttribute("error", error);
//            return "/member/join";
//        }

        Member member = new Member();
        member.setUserId(memberForm.getUserId());
        member.setName(memberForm.getName());
        member.setPassword(memberForm.getPassword());
        member.setPhoneNumber(memberForm.getPhoneNumber());
        member.setEmail(globalEmail);

        memberService.join(member);
        redirectAttributes.addAttribute("memberId", member.getId());
        redirectAttributes.addAttribute("status", true);
        return "redirect:/member/" + member.getId();
    }

    @GetMapping("/login")
    public String loginPage() {
        return "/member/login";
    }

    @PostMapping("/login")
    public String loginExecute(LoginForm loginForm, Model model) {
        String token = makeJwtToken(loginForm.getUserId(), loginForm.getPassword());
        log.info("token : {}",token);
        return "/main";
    }

    @GetMapping("/{memberId}")
    public String checkMemberPage(@PathVariable Long memberId, Model model) {
        Member member = memberService.findById(memberId);
        model.addAttribute("member", member);
        return "/member/checkMember";
    }

    @GetMapping("/memberList")
    public String memberViewPage(Model model) {
        List<Member> allMember = memberService.findByAll();
        model.addAttribute("members", allMember);
        return "/member/memberListView";
    }

    @GetMapping("/emailConfirm")
    public String emailVerify(Model model) {
        model.addAttribute("emailForm", new EmailForm());
        return "/member/emailVerify";
    }

    @PostMapping("/emailConfirm")
    @ResponseBody
    public String emailVerify(
            @RequestParam String email,
            BindingResult bindingResult,
            Model model) throws Exception
    {
//        if(bindingResult.hasErrors()){
//            return "/member/Confirm";
//        }

        globalEmail = email;
        log.info("post email = {}", email);
        String s = emailService.sendSimpleMessage(email);
        emailVerifyCode = s;
        return "redirect:/member/join";
    }

    @PostMapping("/emailVerify")
    @ResponseBody
    public String verifyCode(@RequestParam String emailCheck, Model model) {
        log.info("Post Verify = {}", emailCheck);
        if(!emailVerifyCode.equals(emailCheck)){
            log.info("fail verify");
            emailCheckFlag = false;
            error.put("verifyError", "인증번호가 다릅니다.");
            return "redirect:/member/emailConfirm";
        }
        else {
            log.info("success verify");
            emailCheckFlag = true;
            return "/member/join";
        }
    }

    public boolean hasError() {
        if(!error.isEmpty()) return true;
        return false;
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
    //ResponseEntity<TokenDto>
    public String authorize(String name, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(name, password);

        // authenticate 메소드 실행시 loadUserByUsername 메소드 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 결과를 Security Context에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 그 후 그 인증정보를 기준으로 해서 JWT Token 생성
        String jwt = tokenProvider.createToken(authentication);

        // 그 토큰을 response 헤더에 넣어줌
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + jwt);

        return jwt;
//        return new ResponseEntity<>(new TokenDto(jwt), httpHeaders, HttpStatus.OK);
    }

    public String makeJwtToken(String userId,String password) {
        Date now = new Date();

        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE) // (1)
                .setIssuer("fresh") // (2)
                .setIssuedAt(now) // (3)
                .setExpiration(new Date(now.getTime() + Duration.ofMinutes(30).toMillis())) // (4)
                .claim("userId", userId) // (5)
                .claim("password", password)
                .signWith(SignatureAlgorithm.HS256, "secret") // (6)
                .compact();
    }

}
