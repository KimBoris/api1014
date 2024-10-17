package org.boris.api1014.security.config;


import lombok.RequiredArgsConstructor;
import org.boris.api1014.security.filter.JWTCheckFilter;
import org.boris.api1014.security.util.JWTUtil;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
    @EnableMethodSecurity(prePostEnabled = true)
    @RequiredArgsConstructor
    public class CustomSecurityConfig {

       private final JWTUtil jwtUtil;

        @Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder();
        }

        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

            http.formLogin(config -> config.disable());
            //나이제 폼로그인화면 만들지 않을거야
            http.sessionManagement(config-> config.sessionCreationPolicy(SessionCreationPolicy.NEVER));
            //내부적으로 세션을 만들지도 않을거야

            //그럼 인증할 수 있는 방법은 토큰뿐이다.
            http.csrf(config -> config.disable());

            //가장중요한 핵심적인 요소
            //중간에 끼워넣는다.
            //JWT 문자열을 보내면 서버에서 검사해서 뭔가를 만들어서 시큐리티 안에 넣어준다. 그럼 동작함
            //API서버를 만들때는 동작이 이렇게 약간 다르다.
            http.addFilterBefore(new JWTCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

            http.cors(cors->{
                cors.configurationSource(corsConfigurationSource());
            });

            return http.build();
        }
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {

            CorsConfiguration corsConfiguration = new CorsConfiguration();

            corsConfiguration.setAllowedOriginPatterns(List.of("*")); // 어디서든 허락
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
            corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
            corsConfiguration.setAllowCredentials(true);

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", corsConfiguration);

            return source;
        }
    }
//
//@Configuration
//@EnableMethodSecurity(prePostEnabled = true)
//@RequiredArgsConstructor
//public class CustomSecurityConfig {
//
//    private final JWTUtil jwtUtil;
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http.formLogin(config -> config.disable());
//
//        http.sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.NEVER));
//
//        http.csrf(config -> config.disable());
//
//        http.addFilterBefore(new JWTCheckFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);
//
//        http.cors(cors -> {
//            cors.configurationSource(corsConfigurationSource());
//        });
//
//        return http.build();
//    }
//
//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//
//        corsConfiguration.setAllowedOriginPatterns(List.of("*")); // 어디서든 허락
//        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "HEAD", "OPTIONS"));
//        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type"));
//        corsConfiguration.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//
//        return source;
//    }
//
//}
