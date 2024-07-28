package com.sorisonsoon.user.domain.type;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;

@Getter
public class CustomUser extends User {

    private Long userId;

    public CustomUser(Long userId, String username, String password, UserRole role) {
        super(username, password, Collections.singletonList(new SimpleGrantedAuthority(role.name())));
        this.userId = userId;
    }

    public CustomUser(Long userId, UserDetails userDetails) {
        super(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
        this.userId = userId;
    }
}