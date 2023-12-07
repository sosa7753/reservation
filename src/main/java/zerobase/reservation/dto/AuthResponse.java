package zerobase.reservation.dto;

import lombok.Getter;
import lombok.Setter;
import zerobase.reservation.type.UserType;

import java.util.List;

// 회원가입 및 로그인시 View 정보
@Getter
@Setter
public class AuthResponse {

    private String username;
    private UserType userType;
    private List<String> roles;

    public AuthResponse(Auth.SignUp signUp) {
        this.username = signUp.getUsername();
        this.userType = signUp.getUserType();
        this.roles = signUp.getRoles();
    }
}
