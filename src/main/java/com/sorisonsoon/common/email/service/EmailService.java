package com.sorisonsoon.common.email.service;

import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
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

    private static final String SALT = KeyGenerators.string().generateKey();

    public void sendVerificationEmail(String recipient, String code) throws Exception {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        mimeMessage.setFrom(SENDER_EMAIL);
        mimeMessage.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(recipient));
        mimeMessage.setSubject("[TRIPTROOP] 이메일 인증 요청");
        mimeMessage.setText("인증 번호는 " + code + "입니다.");

        javaMailSender.send(mimeMessage);
    }

    public String generateAuthCode() {
        return UUID.randomUUID().toString().substring(0, 6);
    }

    public String encryptToken(String email, String code) {

        long expiryTime = System.currentTimeMillis() + TOKEN_EXPIRATION * 1000;
        String tokenData = email + ":" + code + ":" + expiryTime;

        TextEncryptor encryptor = Encryptors.text(SECRET, SALT);

        return encryptor.encrypt(tokenData);
    }

    public void verifyToken(String token, String email, String code) {

        TextEncryptor decryptor = Encryptors.text(SECRET, SALT);
        String decryptedData = decryptor.decrypt(token);
        String[] parts = decryptedData.split(":");

        String decryptedEmail = parts[0];
        String decryptedCode = parts[1];
        long expiryTime = Long.parseLong(parts[2]);

        long now = System.currentTimeMillis();
        if (now > expiryTime) {
            throw new IllegalArgumentException("인증 번호의 유효 시간이 만료되었습니다.");
        }

        if (!email.equals(decryptedEmail) || !code.equals(decryptedCode))
            throw new IllegalArgumentException("인증 번호가 올바르지 않습니다.");
    }
}
