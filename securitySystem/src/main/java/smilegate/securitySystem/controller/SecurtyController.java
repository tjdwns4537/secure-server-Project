package smilegate.securitySystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import smilegate.securitySystem.service.SecurityService.SecurityService;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class SecurtyController {

    @Autowired
    private SecurityService securityService;

    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("정상 작동");
    }

    @GetMapping("/principal")
    public Principal retrievePrincipal(Principal principal) {
        return principal;
    }

    @GetMapping("/create/token")
    public Map<String, Object> createToken(@RequestParam(value = "subject") String subject) {
        String token = securityService.createToken(subject, (2 * 1000 * 60));

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("result", token);
        return map;
    }

    @GetMapping("/get/subject")
    public Map<String,Object> getSubject(@RequestParam(value = "token") String token) {
        String subject = securityService.getSubject(token);

        Map<String, Object> map = new LinkedHashMap<>();
        map.put("result", subject);
        return map;
    }
}
