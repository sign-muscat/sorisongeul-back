package com.sorisonsoon.common.swagger.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1")
public class SwaggerController {
    //TODO: Swagger 테스트용 컨트롤러. -> 출력 되는것 확인
    @GetMapping("/main")
    @Operation(summary = "테스트", description = "테스트 API 설명 확인용")
    public String main(){
        return "swagger 예시";
    }
}
