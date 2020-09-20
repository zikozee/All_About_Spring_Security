package com.zikozee.all_spring_security.auth;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.zikozee.all_spring_security.security.ApplicationUserRole.*;
// basically you wanna create another repository, give it a name, e.g Repository("real), implement the ApplicationUserDao with a class and inject the repo in there
//however there must be a findByUsername in the Repo that will be used by the loadByUsername in the MyDetailsService class
//hence u'll have a repo, and a new class that implements this ApplicationDao,
// u can stick with the UserDetails class implemented or extend the fields check "ApplicationUser"  or create your own User and define your fields

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
