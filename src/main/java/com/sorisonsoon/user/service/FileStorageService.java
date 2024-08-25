package com.sorisonsoon.user.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sorisonsoon.user.domain.entity.FileStorageProperties;

@Service
public class FileStorageService {

    private final Path rootLocation = Paths.get("src/main/resources/static/images");

    public String storeFile(MultipartFile file) throws IOException {
        // 파일명 생성
        String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        // 저장 경로 설정
        Path destinationFile = rootLocation.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
        // 폴더가 존재하지 않으면 생성
        if (!Files.exists(destinationFile.getParent())) {
            Files.createDirectories(destinationFile.getParent());
        }
        // 파일 저장
        try (var inputStream = file.getInputStream()) {
            Files.copy(inputStream, destinationFile);
        }
        return fileName;
    }

    public static String getImageUrl(String fileName) {
        return fileName != null ? "/images/" + fileName : null;
    }
}

