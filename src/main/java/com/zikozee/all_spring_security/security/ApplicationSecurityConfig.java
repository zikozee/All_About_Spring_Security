package com.zikozee.all_spring_security.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

import static com.zikozee.all_spring_security.security.ApplicationUserPermission.COURSE_WRITE;
import static com.zikozee.all_spring_security.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // TODO teach in the next session
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*",  "/js/*")
                .permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .anyRequest()
                .authenticated()
                .and()
                .httpBasic();
    }

    @Override
    @Bean // to be instantiated
    public UserDetailsService userDetailsServiceBean() throws Exception {
        UserDetails zikoUser = User.builder()
                .username("ziko")
                .password(passwordEncoder.encode("ziko123"))
                .roles(STUDENT.name()) // ROLE_STUDENT
                .build();

        UserDetails lindaUser= User.builder()
                .username("linda")
                .password(passwordEncoder.encode("password123"))
                .roles(ADMIN.name()) //ROLE_ADMIN
                .build();

        UserDetails tomUser= User.builder()
                .username("tom")
                .password(passwordEncoder.encode("password123"))
                .roles(ADMIN_TRAINEE.name()) //ROLE_ADMIN_TRAINEE
                .build();

        return new InMemoryUserDetailsManager(
                zikoUser,lindaUser, tomUser
        );
    }
}
