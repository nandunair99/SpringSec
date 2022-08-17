package com.narola.springSecurityJPA;

import com.narola.springSecurityJPA.helper.JwtAuthenticationFilter;
import com.narola.springSecurityJPA.helper.UserAuthenticationFilter;
import com.narola.springSecurityJPA.responsehandler.JsonAuthenticationSuccessHandler;
import com.narola.springSecurityJPA.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@ComponentScan(basePackages ="com.narola.springSecurityJPA")
public class SecurityConfig {

    @Autowired
    UserService userDetailService;
    @Autowired
    JsonAuthenticationSuccessHandler jsonAuthenticationSuccessHandler;

    @Autowired
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration builder) throws Exception {
        return builder.getAuthenticationManager();
    }


    @Bean
    public PasswordEncoder encoder() {
        return NoOpPasswordEncoder.getInstance();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.userDetailsService(userDetailService);
        AuthenticationManager authenticationManager = authenticationManagerBuilder.build();
//        authenticationManagerBuilder.inMemoryAuthentication()
//                .withUser("nandu")
//                .password("abc")
//                .roles("USER");
        http
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .csrf().disable()
                .cors().disable()
                .authenticationManager(authenticationManager)
                .authorizeHttpRequests((authz) -> authz
                        .mvcMatchers("/login2").permitAll()
                        .anyRequest().authenticated()
                ).formLogin();
        UserAuthenticationFilter userAuthenticationFilter = new UserAuthenticationFilter("/login2", authenticationManager);

        userAuthenticationFilter.setAuthenticationSuccessHandler(jsonAuthenticationSuccessHandler);
//        userAuthenticationFilter.setAuthenticationSuccessUrl("/registration");

        http.addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();

    }


}

