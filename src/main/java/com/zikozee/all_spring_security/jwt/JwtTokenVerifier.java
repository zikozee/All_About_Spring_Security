package com.zikozee.all_spring_security.jwt;

import com.google.common.base.Strings;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class JwtTokenVerifier extends OncePerRequestFilter {//invoked once per every request
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if(Strings.isNullOrEmpty(authorizationHeader) ||  !authorizationHeader.startsWith("Bearer ")){  //reject request
            filterChain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.replace("Bearer ", ""); //stripping off the Bearer<SPACE> part

        try{
            String secretKey = "securesecuresecuresecuresecuresecuresecuresecuresecuresecuresecure";//must be long // another idea keep key on a different server e.g another machine or Firebase. as JSON n read it with JSON SIMPLE

            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes()))
                    .build()
                    .parseClaimsJws(token);

            //get body or authorities we passed in JwtUsernameAndPasswordAuthenticationFilter.successfulAuthentication
            Claims body = claimsJws.getBody();
            //getting username on login
            String username = body.getSubject();
            //getting our authorities
            var authorities = (List<Map<String, String>>)body.get("authorities");

            //converting authorities to simpleGrantedAuthorities
            Set<SimpleGrantedAuthority> simpleGrantedAuthorities = authorities.stream()
                    .map(m -> new SimpleGrantedAuthority(m.get("authority")))
                    .collect(Collectors.toSet());

            //Initializing authentication with username and its authorities
            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, simpleGrantedAuthorities);
            //setting currently logged in user with its authorities. Hence client that sends request is now authenticated
            SecurityContextHolder.getContext().setAuthentication(authentication);

        }catch (JwtException e){
            throw new IllegalStateException("Token " + token + "cannot be trusted");
        }

    }
}
