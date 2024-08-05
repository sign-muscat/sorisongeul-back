package com.sorisonsoon.common.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "api/v1")
public class SettingTestController {

    private TransformersEmbeddingModel embeddingModel;

    @Autowired
    public SettingTestController(TransformersEmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    //TODO: Swagger 테스트용 컨트롤러. -> 출력 되는것 확인
    @GetMapping("/main")
    @Operation(summary = "테스트", description = "테스트 API 설명 확인용")
    public String main(){
        return "swagger 예시";
    }

//    @GetMapping("/embed")
//    public List<List<Double>> embed() {
//        return embeddingModel.embed(List.of("Hello world", "World is big"));
//    }
@PostMapping("/embed")
public List<Map<String, Object>> embed(@RequestBody List<String> queries) {
    List<String> corpus = List.of(
            "한 남자가 음식을 먹는다.",
            "한 남자가 빵 한 조각을 먹는다.",
            "그 여자가 아이를 돌본다.",
            "한 남자가 말을 탄다.",
            "한 여자가 바이올린을 연주한다.",
            "두 남자가 수레를 숲 속으로 밀었다.",
            "한 남자가 담으로 싸인 땅에서 백마를 타고 있다.",
            "원숭이 한 마리가 드럼을 연주한다.",
            "치타 한 마리가 먹이 뒤에서 달리고 있다."
    );

    List<Map<String, Object>> results = new ArrayList<>();

    // 코퍼스 임베딩 계산
    List<List<Double>> corpusEmbeddings = embeddingModel.embed(corpus);

    for (String query : queries) {
        Map<String, Object> result = new HashMap<>();
        result.put("query", query);

        // 쿼리 임베딩 계산
        List<List<Double>> queryEmbedding = embeddingModel.embed(List.of(query));

        // 코사인 유사도 계산
        List<Double> cosScores = new ArrayList<>();
        for (List<Double> corpusEmbedding : corpusEmbeddings) {
            double score = cosineSimilarity(queryEmbedding.get(0), corpusEmbedding);
            cosScores.add(score);
        }

        // 상위 5개의 결과 찾기
        List<Map<String, Object>> topResults = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            int maxIndex = cosScores.indexOf(cosScores.stream().max(Double::compareTo).orElse(0.0));
            Map<String, Object> topResult = new HashMap<>();
            topResult.put("sentence", corpus.get(maxIndex));
            topResult.put("score", cosScores.get(maxIndex));
            topResults.add(topResult);
            cosScores.set(maxIndex, -1.0);  // 다음 최대값을 찾기 위해 이미 선택된 값을 제거
        }

        result.put("topResults", topResults);
        results.add(result);
    }

    return results;
}

    // 코사인 유사도 계산 함수
    private double cosineSimilarity(List<Double> vectorA, List<Double> vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.size(); i++) {
            dotProduct += vectorA.get(i) * vectorB.get(i);
            normA += Math.pow(vectorA.get(i), 2);
            normB += Math.pow(vectorB.get(i), 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
}
