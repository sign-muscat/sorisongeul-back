package com.sorisonsoon.user.domain.type;

import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collections;

@Getter
public class CustomUser extends User {

    private Long userId;

    public CustomUser(Long userId, String id, String password, UserRole role) {
        super(id, password, Collections.singletonList(new SimpleGrantedAuthority(role.name())));
        this.userId = userId;
    }

}