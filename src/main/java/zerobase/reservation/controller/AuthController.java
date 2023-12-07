package zerobase.reservation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zerobase.reservation.domain.MemberEntity;
import zerobase.reservation.dto.Auth;
import zerobase.reservation.dto.AuthResponse;
import zerobase.reservation.security.TokenProvider;
import zerobase.reservation.service.MemberService;

@Slf4j
@RequestMapping("/auth")
@RestController
@RequiredArgsConstructor
public class AuthController {

    private final MemberService memberService;
    private final TokenProvider tokenProvider;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody Auth.SignUp request) {
        AuthResponse result = this.memberService.register(request);
        log.info("success : " + result.getUsername());
        return ResponseEntity.ok(result);
    }

    // 로그인
    // 1. 비밀번호 검증 2. 토큰 생성 후 반환
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody Auth.SignIn request) {
        MemberEntity member = this.memberService.authenticate(request);
        String token = this.tokenProvider.generateToken
                (member.getUsername(), member.getRoles(), member.getUserType());
        log.info("user login -> " + request.getUsername());
        return ResponseEntity.ok(token);
    }
}
