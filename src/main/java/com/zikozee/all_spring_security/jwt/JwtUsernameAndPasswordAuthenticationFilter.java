package com.zikozee.all_spring_security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;

//THIS HANDLES CREDENTIAL SENT AND VALIDATION OF THE CREDENTIALS

@Slf4j
public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //Note: we checked the implementation of Authentication interface n the one we need is UsernamePasswordAuthenticationToken as below

        try{
            UsernameAndPasswordAuthenticationRequest authenticationRequest = new ObjectMapper()
                    .readValue(request.getInputStream(), UsernameAndPasswordAuthenticationRequest.class);


            Authentication authentication = new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(),
                            authenticationRequest.getPassword());

            //authentication manager checks if username exists if so, then checks if password is correct, if so returns authenticated
            Authentication authenticate = authenticationManager.authenticate(authentication);
            return authenticate;
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    //THIS METHOD IS INVOKED AFTER attemptAuthentication Above is successful else this method never gets executed
    //Here we generate the token and send to the client
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException, ServletException {

        log.info("IS USERNAME CORRECT::-->> " + authResult.getName());

        String key = "securesecuresecuresecuresecuresecuresecuresecuresecuresecuresecure"; // another idea keep key on a different server e.g another machine or Firebase. as JSON n read it with JSON SIMPLE
        String token = Jwts.builder()
                .setSubject(authResult.getName())//this is supposed to return usernames in DB, if USER is returned use (User)authResult.getPrincipal(), assign to a variable user and do user.getUsername()
                .claim("authorities", authResult.getAuthorities())// body or Payload data
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusWeeks(2))) //duration
                .signWith(Keys.hmacShaKeyFor(key.getBytes())) //signature
                .compact();

        //sending token back
        response.setHeader("Authorization", "Bearer " + token);

        super.successfulAuthentication(request, response, chain, authResult);
    }
}
