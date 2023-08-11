package com.springboot.blog.config;

import com.springboot.blog.security.JwtAuthenticationEntryPoint;
import com.springboot.blog.security.JwtAuthenticationnFilter;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableMethodSecurity
@SecurityScheme(
        name = "Bear Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    @Autowired
    private JwtAuthenticationnFilter jwtAuthenticationnFilter;
    @Bean
    public static PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    //se tu dong cung cap UserDetails va Password thanh authentication manager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
       //bat ki yeu cau nao deu da dc xac thuc
        httpSecurity.csrf( c ->
                c.disable())
                .authorizeHttpRequests(author ->
                        //author.anyRequest().authenticated()
                     author.requestMatchers(HttpMethod.GET,"/api/**").permitAll()
                             .requestMatchers("/api/auth/**").permitAll()
                             .requestMatchers("/swagger-ui/**").permitAll()
                             .requestMatchers("/v3/api-docs/**").permitAll()
                             .anyRequest().authenticated()
                        ).exceptionHandling(exception -> exception
                //Cấu hình điểm bắt đầu xác thực (authentication entry point) khi xảy ra lỗi xác thực.
                //Cấu hình quản lý phiên (session management),
                        //ở đây yêu cầu sử dụng chế độ phiên STATELESS (không lưu trạng thái phiên).
                        .authenticationEntryPoint(jwtAuthenticationEntryPoint)).sessionManagement(
                                session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                );

        // Điều này đảm bảo rằng xác thực qua JWT được thực hiện
        // trước khi xử lý xác thực thông qua tên người dùng và mật khẩu.
        httpSecurity.addFilterBefore(jwtAuthenticationnFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

//    @Bean
//    public UserDetailsService userDetailsService(){
//        //tao ra mot vai nguoi dung va luu tru trong bo nho
//        UserDetails nghia = User.builder()
//                .username("nghia")
//                .password(passwordEncoder().encode("nghia"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.builder()
//                .username("admin")
//                .password(passwordEncoder().encode("admin"))
//                .roles("ADMIN")
//                .build();
//
//        return new InMemoryUserDetailsManager(nghia, admin);
//    }
}
