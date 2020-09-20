package com.zikozee.all_spring_security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ApplicationUserDetailsService implements UserDetailsService {

    @Qualifier("fake")//necessary if we have more than one implementation, check comment by ctrl clicking fake
    private final ApplicationUserDao applicationUserDao;

    @Override
    public UserDetails loadUserByUsername(String username){
        return applicationUserDao.selectApplicationByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username "+ username +" not found"));
    }
}
