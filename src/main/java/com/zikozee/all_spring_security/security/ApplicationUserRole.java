package com.zikozee.all_spring_security.security;

import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.zikozee.all_spring_security.security.ApplicationUserPermission.*;

@RequiredArgsConstructor
@Getter
public enum ApplicationUserRole {
    //we used guava
    STUDENT(Sets.newHashSet()), //no permission
    ADMIN(Sets.newHashSet(COURSE_READ, COURSE_WRITE, STUDENT_READ, STUDENT_WRITE)),
    ADMIN_TRAINEE(Sets.newHashSet(COURSE_READ, STUDENT_READ));

    private final Set<ApplicationUserPermission> permissions;

    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> grantedPermissions = permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());

        grantedPermissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return grantedPermissions;
    }
}
