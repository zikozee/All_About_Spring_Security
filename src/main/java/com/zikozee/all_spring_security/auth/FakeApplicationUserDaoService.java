package com.zikozee.all_spring_security.auth;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zikozee.all_spring_security.security.ApplicationUserRole.*;

@Repository("fake")
@RequiredArgsConstructor
public class FakeApplicationUserDaoService implements ApplicationUserDao{

    private final PasswordEncoder passwordEncoder;


    @Override
    public Optional<ApplicationUser> selectApplicationByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> applicationUser.getUsername().equals(username))
                .findFirst();
    }


    private List<ApplicationUser> getApplicationUsers(){

        return Lists.newArrayList(
            new ApplicationUser(
                    "ziko",
                    passwordEncoder.encode("ziko123"),
                    STUDENT.getGrantedAuthorities(),
                    true,
                    true,
                    true,
                    true
            ),
                new ApplicationUser(
                        "linda",
                        passwordEncoder.encode("password"),
                        ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "tom",
                        passwordEncoder.encode("password"),
                        ADMIN_TRAINEE.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                ),
                new ApplicationUser(
                        "debby",
                        passwordEncoder.encode("password"),
                        SUPER_ADMIN.getGrantedAuthorities(),
                        true,
                        true,
                        true,
                        true
                )
        );
    }
}
