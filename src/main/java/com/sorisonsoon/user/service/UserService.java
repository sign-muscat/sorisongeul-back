package com.sorisonsoon.user.service;
import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sorisonsoon.user.domain.entity.User;
import com.sorisonsoon.user.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    // 사용자 정보 조회
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }

    // 사용자 정보 업데이트
    public boolean updateUser(User user) {
        if (userRepository.existsById(user.getUserId())) {
            userRepository.save(user);
            return true;
        }
        return false;
    }

    // 프로필 사진 저장
    public String saveProfilePicture(MultipartFile file) throws IOException {
        // 파일 저장 로직을 FileStorageService에 위임
        String fileName = fileStorageService.storeFile(file);
        return fileStorageService.getImageUrl() + fileName;
    }

    // 비밀번호 해시화
    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("존재 하지 않는 사용자입니다: " + email));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name()) // 회원 권한을 role로 설정
                .build();
    }

    public User saveUser(User user) {
        validateDuplicateUser(user);
        return userRepository.save(user);
    }

    private void validateDuplicateUser(User user) {
        userRepository.findByEmail(user.getEmail())
                .ifPresent(existingUser -> {
                    throw new IllegalStateException("이미 가입된 이메일입니다.");
                });
        userRepository.findById(user.getId())
                .ifPresent(existingUser -> {
                    throw new IllegalStateException("이미 가입된 아이디입니다.");
                });
    }

    public boolean isIdAvailable(String id) {
        return userRepository.findById(id).isEmpty();
    }

    public boolean isEmailAvailable(String email) {
        return userRepository.findByEmail(email).isEmpty();
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원 정보를 찾을 수 없습니다."));
    }

    @Transactional
    public void deleteUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 사용자 삭제 로직
        userRepository.delete(user);
    }
}
