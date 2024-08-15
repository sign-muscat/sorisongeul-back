package com.sorisonsoon.user.service;


import com.sorisonsoon.common.exception.AuthException;
import com.sorisonsoon.common.security.dto.LoginDto;
import com.sorisonsoon.common.security.dto.TokenDto;
import com.sorisonsoon.common.security.util.TokenUtils;
import com.sorisonsoon.user.domain.entity.User;
import com.sorisonsoon.user.domain.repository.UserRepository;
import com.sorisonsoon.user.domain.type.CustomUser;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

import static com.sorisonsoon.common.exception.type.ExceptionCode.INVALID_REFRESH_TOKEN;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TokenUtils tokenUtils;

    @Override
    public CustomUser loadUserByUsername(String id) throws UsernameNotFoundException {
        //return null;
        return userRepository.findById(id)
                .map(user -> new CustomUser(
                        user.getUserId(),
                        user.getId(),
                        user.getPassword(),
                        user.getRole()
                ))
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    /**
     * RefreshToken 으로 조회
     */
    public User findByRefreshToken(String refreshToken) {

        return userRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new AuthException(INVALID_REFRESH_TOKEN));
    }


    /**
     * AccessToken 발급
     */
    public String issueToken(String refreshToken, String id) {

        final User user = findByRefreshToken(refreshToken);
        LoginDto loginDto = LoginDto.from(user);
        CustomUser customUser = loginDto.toCustomUser();

        if (!tokenUtils.isValidToken(refreshToken, loadUserByUsername(id))) {
            throw new AuthException(INVALID_REFRESH_TOKEN);
        }

        return tokenUtils.createAccessToken(customUser);
    }

    /**
     * RefreshToken 검증 및 재발급
     */
    @Transactional
    public TokenDto checkRefreshTokenAndReIssueToken(String refreshToken) {

        User user = findByRefreshToken(refreshToken);
        LoginDto loginDto = LoginDto.from(user);
        CustomUser customUser = loginDto.toCustomUser();

        String reIssuedRefreshToken = tokenUtils.createRefreshToken();
        String reIssuedAccessToken = tokenUtils.createAccessToken(customUser);

        user.updateRefreshToken(reIssuedRefreshToken);

        return TokenDto.of(reIssuedAccessToken, reIssuedRefreshToken);
    }

    /**
     * Authentication 저장
     */
    public void saveAuthentication(String id) {

        CustomUser user = loadUserByUsername(id);

        Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private Map<String, Object> getUserInfo(LoginDto loginDto) {
        return Map.of(
                "id", loginDto.getId(),
                "role", loginDto.getRole()
        );
    }

}
