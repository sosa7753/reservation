package zerobase.reservation.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtauthenticationFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable())
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers(
                        "/store/read/**", "/store/autocomplete/**",
                        "/reservation/update", "/reservation/delete",
                        "/reservation/read", "/visit/delete", "/visit/read"
                ).hasAnyRole("CLIENT", "ADMIN")
                .requestMatchers(
                        "/reservation/register", "/visit/arrive", "/visit/update"
                ).hasRole("CLIENT")
                .requestMatchers(
                        "/store/register", "/store/update", "store/delete/**"
                ).hasRole("ADMIN")
                .requestMatchers(
                        "/**/signup", "/**/signin","/error"
                ).permitAll()
                .anyRequest().authenticated()
        );
        http.addFilterBefore(this.jwtauthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

