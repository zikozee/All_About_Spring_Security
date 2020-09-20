package com.zikozee.all_spring_security.auth;

import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;

//@RequiredArgsConstructor
@Setter
@AllArgsConstructor
public class ApplicationUser implements UserDetails {
    //you can set as Entity and add more Fields as needed for the user
    //And u can basically use a custom constructor for the needed fields
    //since getters are already available add custom setters for the ones you need to expose
    //however i switched them from private final to just private to allow setters
    //however to add extra fields think of how you want the constructor to be defined, so rsther than use annotation, create custom annotation

    private String username;
    private String password;
    private Set<? extends GrantedAuthority> grantedAuthorities;
    private boolean isAccountNonExpired;
    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isEnabled;
    //private boolean farmer;

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return grantedAuthorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}
