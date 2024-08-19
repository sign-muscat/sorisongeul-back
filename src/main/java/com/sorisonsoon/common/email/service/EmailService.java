package com.sorisonsoon.common.email.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.security.crypto.keygen.KeyGenerators;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String SENDER_EMAIL;

    @Value("${jwt.email.secret}")
    private String SECRET;

    @Value("${jwt.email.expiration}")
    private Long TOKEN_EXPIRATION;

    public void sendVerificationEmail(String recipient, String code) throws Exception {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "UTF-8");

        helper.setFrom(SENDER_EMAIL);
        helper.setTo(recipient);
        helper.setSubject("[소리손순] 이메일 인증 요청");
        System.out.println("Subject: " + mimeMessage.getSubject());
        String htmlContent = "<div style='text-align: center;'>"
                + "    <img src='cid:logoImage' alt='소리손순 로고' style='width:57px; margin-bottom:20px;' />"
                + "    <h2>이메일 인증 요청</h2>"
                + "    <p>안녕하세요, 소리손순 입니다.</p>"
                + "    <p>요청하신 인증 번호는 <strong style='font-size: 1.2em;'>[" + code + "]</strong> 입니다.</p>"
                + "    <p>감사합니다!</p>"
                + "</div>";

        helper.setText(htmlContent, true);
        ClassPathResource image = new ClassPathResource("static/images/logo.png");
        helper.addInline("logoImage", image);

        javaMailSender.send(mimeMessage);
    }


    public String generateAuthCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public String encryptToken(String email, String code) {
        long expiryTime = System.currentTimeMillis() + TOKEN_EXPIRATION * 1000;
        String tokenData = email + ":" + code + ":" + expiryTime;

        // 유동적인 SALT 생성
        String salt = KeyGenerators.string().generateKey();
        TextEncryptor encryptor = Encryptors.text(SECRET, salt);

        // SALT를 암호화된 토큰과 함께 저장
        String encryptedData = encryptor.encrypt(tokenData);
        return salt + ":" + encryptedData;
    }

    public void verifyToken(String token, String email, String code) {
        // 토큰에서 SALT와 암호화된 데이터를 분리
        String[] tokenParts = token.split(":");
        String salt = tokenParts[0];
        String encryptedData = tokenParts[1];

        TextEncryptor decryptor = Encryptors.text(SECRET, salt);
        String decryptedData = decryptor.decrypt(encryptedData);

        String[] parts = decryptedData.split(":");
        String decryptedEmail = parts[0];
        String decryptedCode = parts[1];
        long expiryTime = Long.parseLong(parts[2]);

        long now = System.currentTimeMillis();
        if (now > expiryTime) {
            throw new IllegalArgumentException("인증 번호의 유효 시간이 만료되었습니다.");
        }

        if (!email.equals(decryptedEmail) || !code.equals(decryptedCode)) {
            throw new IllegalArgumentException("인증 번호가 올바르지 않습니다.");
        }
    }
}

