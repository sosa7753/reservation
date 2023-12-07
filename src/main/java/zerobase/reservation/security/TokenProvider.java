package zerobase.reservation.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import zerobase.reservation.service.MemberService;
import zerobase.reservation.type.UserType;

import java.util.Date;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TokenProvider {

    private static final long TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간
    private static final String KEY_ROLES = "roles";
    private static final String KEY_USERS = "userType";

    private final MemberService memberService;

    @Value("{spring.jwt.secret}")
    private String secretKey;

    // jwt 토큰 생성
    public String generateToken(String username, List<String> roles, UserType userType) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(KEY_ROLES, roles); // 행동 권한 추가
        claims.put(KEY_USERS, userType); // 사용자 추가

        Date now = new Date(); // 현재 시간
        Date expiredDate = new Date(now.getTime() + TOKEN_EXPIRE_TIME);

        return Jwts.builder()
                .setClaims(claims) // 권한
                .setIssuedAt(now) // 생성 시간
                .setExpiration(expiredDate) // 만료 시간
                .signWith(SignatureAlgorithm.HS512, this.secretKey) // 암호화 알고리즘 및 비밀키
                .compact();
    }

    // Filter 에서 토큰 유효성 검증 부분

    public Authentication getAuthentication(String jwt) { // jwt로 부터 인증정보 가져옴
        UserDetails userDetails = this.memberService.loadUserByUsername(this.ParsedClaims(jwt));
        return new UsernamePasswordAuthenticationToken
                (userDetails, "", userDetails.getAuthorities());
    }

    public String ParsedClaims(String token) {
        return this.parsingClaims(token).getSubject();
    }

    private Claims parsingClaims(String token) {
        try {
            return Jwts.parser().setSigningKey(this.secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }

    public boolean validateToken(String token) {
        if(!StringUtils.hasText(token)) { // 토큰이 "null", 공백 인지 판별
            return false;
        }

        Claims claims = this.parsingClaims(token);
        return !claims.getExpiration().before(new Date());
    }
}
