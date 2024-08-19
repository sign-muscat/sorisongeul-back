package com.sorisonsoon.interest.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service
public class WordCloudGenerator {

    public void generateWordCloud(String keywords) throws IOException {
        // 키워드를 임시 파일에 저장
        File tempFile = new File("keywords.txt");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile))) {
            writer.write(keywords);
        }

        // Python 스크립트 실행
        ProcessBuilder processBuilder = new ProcessBuilder("python", "path/to/wordcloud_script.py", tempFile.getAbsolutePath());
        Process process = processBuilder.start();

        // Python 스크립트의 출력 읽기
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

