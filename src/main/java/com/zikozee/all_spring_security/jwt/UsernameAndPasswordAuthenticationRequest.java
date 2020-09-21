package com.zikozee.all_spring_security.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UsernameAndPasswordAuthenticationRequest {

    private String username;
    private String password;
}
