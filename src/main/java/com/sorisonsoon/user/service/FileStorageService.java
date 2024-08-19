package com.sorisonsoon.user.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sorisonsoon.user.domain.entity.FileStorageProperties;

@Service
public class FileStorageService {

    private final FileStorageProperties properties;

    @Autowired
    public FileStorageService(FileStorageProperties properties) {
        this.properties = properties;
    }

    public String storeFile(MultipartFile file) throws IOException {
        Path path = Paths.get(properties.getImageDir());
        if (!Files.exists(path)) {
            Files.createDirectories(path);
        }
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = path.resolve(fileName);
        file.transferTo(filePath.toFile());
        System.out.println("File saved to: " + filePath.toString());
        return fileName;
    }

    public String getImageUrl() {
        return properties.getImageUrl();
    }
}
