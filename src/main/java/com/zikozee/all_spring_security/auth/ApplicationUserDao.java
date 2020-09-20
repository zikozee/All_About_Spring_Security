package com.zikozee.all_spring_security.auth;

import java.util.Optional;

public interface ApplicationUserDao {

    Optional<ApplicationUser> selectApplicationByUsername(String username);
}
