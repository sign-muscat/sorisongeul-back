package com.sorisonsoon.user.service;


import com.sorisonsoon.user.domain.type.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {


    @Override
    public CustomUser loadUserByUsername(String id) throws UsernameNotFoundException {
        return null;
    }

}
