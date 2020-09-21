package com.zikozee.all_spring_security.security;

import com.zikozee.all_spring_security.auth.ApplicationUserDetailsService;
import com.zikozee.all_spring_security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.concurrent.TimeUnit;

import static com.zikozee.all_spring_security.security.ApplicationUserPermission.COURSE_WRITE;
import static com.zikozee.all_spring_security.security.ApplicationUserRole.*;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true) // method based role permission
public class ApplicationSecurityConfig extends WebSecurityConfigurerAdapter {

    private final PasswordEncoder passwordEncoder;
    private final ApplicationUserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
//                .csrf(csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
                .csrf().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)//session will no longer be stored in a database
                .and()
                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager())) //authenticationManager() is available as a protected method in WebSecurityConfigurerAdapter, hence we can use directly
                .authorizeRequests()
                .antMatchers("/", "index", "/css/*",  "/js/*")
                .permitAll()
                .antMatchers("/api/**").hasRole(STUDENT.name())
                .anyRequest()
                .authenticated();
//                .and()
//                //.httpBasic(); //Basic Authentication
//                    .formLogin()
//                    .loginPage("/login").permitAll()// form based authentication
//                    .defaultSuccessUrl("/courses", true)
//                   // .usernameParameter("userxyz")//this must match the one in form login
//                  //  .passwordParameter("passxyz")//this must match the one in form login
//                .and()
//                .rememberMe()// defaults to 2 weeks
//                //.rememberMeParameter("rememberxyz")//this must match the one in form login
//                .tokenValiditySeconds((int) TimeUnit.DAYS.toSeconds(21)) //overriding remember me to 21 days
//                .key("someSecretKey###@$")//overriding default key
//                .userDetailsService(userDetailsService)
//                .and()
//                .logout()
//                    .logoutUrl("/logout")//same as default, we can change
//                    . logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET"))
//                    .clearAuthentication(true)
//                    .invalidateHttpSession(true)
//                    .deleteCookies("JSESSIONID", "remember-me")
//                    .logoutSuccessUrl("/login");

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public  DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);

        return provider;
    }

//    @Override
//    @Bean // to be instantiated
//    public UserDetailsService userDetailsServiceBean() throws Exception {
//        //Highlighting on UserDetails we see  Collection<? extends GrantedAuthority> getAuthorities();
//        //which indicates that UserDetails only have concepts of GrantedAuthorities and NOT roles NOR permissions hence the reason
//        // we built grantedAuthorities( in ApplicationUserRole Enum) from the permissions assigned to each role
//        //note: roles(Highlighting it) takes list of roles i.e String... roles
//        //GrantedAuthority is an interface with one of its implementation as -> SimpleGrantedAuthority
//
//        UserDetails zikoUser = User.builder()
//                .username("ziko")
//                .password(passwordEncoder.encode("ziko123"))
////                .roles(STUDENT.name()) // ROLE_STUDENT
//                .authorities(STUDENT.getGrantedAuthorities())
//                .build();
//
//        UserDetails lindaUser= User.builder()
//                .username("linda")
//                .password(passwordEncoder.encode("password123"))
////                .roles(ADMIN.name()) //ROLE_ADMIN
//                .authorities(ADMIN.getGrantedAuthorities())
//                .build();
//
//        UserDetails tomUser= User.builder()
//                .username("tom")
//                .password(passwordEncoder.encode("password123"))
////                .roles(ADMIN_TRAINEE.name()) //ROLE_ADMIN_TRAINEE
//                .authorities(ADMIN_TRAINEE.getGrantedAuthorities())
//                .build();
//
//        UserDetails debbyUser= User.builder()
//                .username("debby")
//                .password(passwordEncoder.encode("password123"))
////                .roles(ADMIN_TRAINEE.name()) //ROLE_ADMIN_TRAINEE
//                .authorities(SUPER_ADMIN.getGrantedAuthorities())
//                .build();
//
//        return new InMemoryUserDetailsManager(
//                zikoUser,lindaUser, tomUser, debbyUser
//        );
//    }
}
