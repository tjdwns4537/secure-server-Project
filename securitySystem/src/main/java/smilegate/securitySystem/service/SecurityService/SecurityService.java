package smilegate.securitySystem.service.SecurityService;

import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class SecurityService {

    private static final String SECRET_KEY = "awerprl242wperl1wpe2rlwperlpwelrpwel3rproawerwaoefjoiawjvoiajowdjfaowijfo";

    // 로그인 서비스 던질 때 같이 진행
    public String createToken(String subject, long expTime) {
        if (expTime <= 0) {
            throw new RuntimeException("만료시간이 0보다 커야함");
        }
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        byte[] secretKeyByte = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signInKey = new SecretKeySpec(secretKeyByte, signatureAlgorithm.getJcaName());

        return Jwts.builder()
                .setSubject(subject)
                .signWith(signInKey, signatureAlgorithm)
                .setExpiration(new Date(System.currentTimeMillis() + expTime))
                .compact();
    }

    // 토큰 검증하는 메서드를 boolean 타입으로 써서 검증
    public String getSubject(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }
}
