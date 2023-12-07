package zerobase.reservation.service;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import zerobase.reservation.domain.MemberEntity;
import zerobase.reservation.dto.Auth;
import zerobase.reservation.dto.AuthResponse;
import zerobase.reservation.exception.ReservationException;
import zerobase.reservation.repository.MemberRepository;
import zerobase.reservation.type.ErrorCode;

@Slf4j
@Service
@AllArgsConstructor
@Builder
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.memberRepository.findByUsername(username)
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_USER_NAME));
    }

    // 계정 등록
    public AuthResponse register(Auth.SignUp request) {
        boolean exists = this.memberRepository.existsByUsername(request.getUsername());
        if(exists) {
            throw new ReservationException(ErrorCode.ALREADY_USE_NAME);
        }

        request.setPassword(this.passwordEncoder.encode(request.getPassword()));
        this.memberRepository.save(request.toEntity());

        return new AuthResponse(request);


    }

    // 비밀번호 검증
    public MemberEntity authenticate(Auth.SignIn member) {

        MemberEntity user = this.memberRepository.findByUsername(member.getUsername())
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_USER_NAME));

        if(!this.passwordEncoder.matches(member.getPassword(), user.getPassword())) {
            throw new ReservationException(ErrorCode.NOT_EQUALS_PASSWORD);
        }
        return user;
    }
}
