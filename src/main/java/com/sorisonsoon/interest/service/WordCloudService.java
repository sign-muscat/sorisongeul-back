package com.sorisonsoon.interest.service;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class WordCloudService {

    @Async
    public void runPythonScript(String keyword) {
        try {
            // 올바른 Python 인터프리터 경로를 설정합니다
            String pythonInterpreterPath = "C:\\Users\\hi02\\miniforge3\\envs\\fastt\\python.exe";
            File scriptFile = new File("simText.py");

            // 스크립트 파일이 현재 작업 디렉토리에 있는지 확인합니다
            if (!scriptFile.exists()) {
                System.err.println("스크립트 파일이 존재하지 않습니다: " + scriptFile.getAbsolutePath());
                return;
            }

            ProcessBuilder processBuilder = new ProcessBuilder(pythonInterpreterPath, scriptFile.getAbsolutePath(), keyword);
            processBuilder.directory(new File(System.getProperty("user.dir"))); // 현재 루트 디렉토리에서 실행

            Process process = processBuilder.start();
            try (BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));
                 BufferedReader stdError = new BufferedReader(new InputStreamReader(process.getErrorStream()))) {

                String s;
                while ((s = stdInput.readLine()) != null) {
                    System.out.println("stdout: " + s); // 로깅으로 변경할 수 있습니다
                }

                while ((s = stdError.readLine()) != null) {
                    System.err.println("stderr: " + s); // 로깅으로 변경할 수 있습니다
                }
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("simText.py 실행 중 오류 발생: exitCode=" + exitCode); // 로깅으로 변경할 수 있습니다
            }
        } catch (Exception e) {
            System.err.println("관심사 수정 중 오류 발생: " + e.getMessage()); // 로깅으로 변경할 수 있습니다
        }
    }

}
