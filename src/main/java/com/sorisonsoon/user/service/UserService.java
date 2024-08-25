package com.sorisonsoon.user.service;
import java.io.IOException;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sorisonsoon.common.exception.NotFoundException;
import com.sorisonsoon.common.exception.type.ExceptionCode;
import com.sorisonsoon.user.domain.entity.User;
import com.sorisonsoon.user.domain.repository.UserRepository;
import com.sorisonsoon.user.dto.response.NickNameUserInfo;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final FileStorageService fileStorageService;

    // 사용자 정보 조회
    public User findById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }


    public boolean updateUser(User user) {
        if (userRepository.existsById(user.getUserId())) {
            userRepository.save(user);
            return true;
        }
        return false;
    }

    private final S3Service s3Service;

    public Long updateUser(Long userId, String userNickname, MultipartFile image) throws IOException {
        User findUser = userRepository.findById(userId).orElse(null);

        if (!image.isEmpty()) {
            String profileImage = s3Service.upload(image, "profileImage");
            findUser.update(userId, userNickname, profileImage);
        } else {
            findUser.update(userId, null, null);
        }

        return findUser.getUserId();
    }

//    private final Path rootLocation = Paths.get("src/main/resources/static/images"); // 저장 경로
//
//    // 파일 저장 메서드
//    public String storeFile(MultipartFile file) throws IOException {
//        return fileStorageService.storeFile(file);
//    }
//
//    public String getImageUrl(String fileName) {
//        return "/images/" + fileName; // URL 반환
//    }
//
//    // 기존 프로필 사진 삭제
//    private void deleteExistingProfileImage(String fileName) throws IOException {
//        if (fileName != null && !fileName.isEmpty()) {
//            Path filePath = rootLocation.resolve(fileName).normalize().toAbsolutePath();
//            Files.deleteIfExists(filePath);
//        }
//    }

//    // 프로필 사진 업데이트
//    public void updateProfileImage(Long userId, MultipartFile newProfileImage) throws IOException {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER));
//
//        // 기존 프로필 이미지 파일 삭제
//        String existingImage = user.getProfileImage();
//        deleteExistingProfileImage(existingImage);
//
//        // 새로운 프로필 이미지 저장
//        String newImageFileName = storeFile(newProfileImage);
//        String newImageUrl = getImageUrl(newImageFileName);
//
//        // 사용자 엔티티에 새로운 이미지 URL 설정
//        user.setProfileImage(newImageUrl);
//        userRepository.save(user);
//    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER + email));

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
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER));
    }

    @Transactional
    public void deleteUser(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER));

        if (!user.getPassword().equals(password)) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // 사용자 삭제 로직
        userRepository.delete(user);
    }

    public NickNameUserInfo getUserNickname(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ExceptionCode.NOT_FOUND_USER));
        return new NickNameUserInfo(user.getUserId(), user.getId(), user.getNickname(), user.getEmail(), user.getProfileImage());
    }
}