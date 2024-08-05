package com.sorisonsoon.common.controller;

import com.sorisonsoon.common.utils.sentenceSimilarity.MatrixUtils;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@RestController
@RequiredArgsConstructor
@RequestMapping(value = "api/v1")
public class SettingTestController {

    private final TransformersEmbeddingModel transformersEmbeddingModel;

    //Swagger 테스트용 컨트롤러 코드
    @GetMapping("/main")
    @Operation(summary = "테스트", description = "테스트 API 설명 확인용")
    public String main(){
        return "swagger 예시";
    }


    //SentenceSimilarity 테스트용 컨트롤러 코드
    @PostMapping("/embed/1")
    public EmbeddingResponse embedText(@RequestBody List<String> texts) {
        return transformersEmbeddingModel.embedForResponse(texts);
    }

    @PostMapping("/embed/2")
    public double[][] getEmbeddings(@RequestBody List<String> texts) throws Exception {
        return transformersEmbeddingModel.embed(texts).stream()
                .map(l -> l.stream().mapToDouble(Double::doubleValue).toArray())
                .toArray(double[][]::new);
    }

    @PostMapping("/similarity")
    public double getSimilarity(@RequestBody List<String> texts) throws Exception {
        if (texts.size() != 2) {
            throw new IllegalArgumentException("2개의 텍스트를 배열로 입력해 주세용");
        }
        double[][] embeddings = getEmbeddings(texts);
        return MatrixUtils.pairwiseCosineSimilarity(embeddings, embeddings)[0][1];
    }

    @PostMapping("/top-similar")
    public List<String> getTopSimilar(@RequestBody List<String> input) throws Exception {
        if (input.size() < 2) {
            throw new IllegalArgumentException("2개 이상의 텍스트를 입력해 주세용. 처음 한개가 비교 값이 되고 나머지 가장 유사한 5개까지 보여 집니당");
        }
        List<String> queries = input.subList(0, 1);
        List<String> corpus = input.subList(1, input.size());

        double[][] queryEmbeddings = getEmbeddings(queries);
        double[][] corpusEmbeddings = getEmbeddings(corpus);

        double[][] cosineSimilarity = MatrixUtils.pairwiseCosineSimilarity(queryEmbeddings, corpusEmbeddings);
        int[] argsort = MatrixUtils.argsort(cosineSimilarity[0], false);

        return IntStream.range(0, Math.min(5, argsort.length))
                .mapToObj(i -> corpus.get(argsort[i]))
                .collect(Collectors.toList());
    }

}
