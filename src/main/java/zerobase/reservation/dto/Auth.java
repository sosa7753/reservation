package zerobase.reservation.dto;


import lombok.Data;
import zerobase.reservation.domain.MemberEntity;
import zerobase.reservation.type.UserType;

import java.util.List;

public class Auth {

    @Data
    public static class SignIn {

        private String username;
        private String password;

    }
    @Data
    public static class SignUp {
        private String username;
        private String password;

        private UserType userType;

        private List<String> roles; // 행동 역할

        public MemberEntity toEntity() {
            return MemberEntity.builder()
                    .username(this.username)
                    .password(this.password)
                    .userType(this.userType)
                    .roles(this.roles)
                    .build();
        }
    }
}
