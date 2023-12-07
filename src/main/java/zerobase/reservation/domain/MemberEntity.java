package zerobase.reservation.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import zerobase.reservation.type.UserType;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "member")
public class MemberEntity implements UserDetails {

    @Id
    private String username;

    @JsonIgnore // 직렬화, 역 직렬화 무시
    private String password;

    @Enumerated(EnumType.STRING)
    private UserType userType; // 점장인지 사용자인지 구분

    // 권한
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> roles; // 행동 역할

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();

        // roles 추가
        authorities.addAll((this.roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList())));

        // 사용자 권한 추가
        authorities.add(new SimpleGrantedAuthority(this.userType.name()));

        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}
