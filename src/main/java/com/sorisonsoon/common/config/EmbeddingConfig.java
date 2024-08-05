package com.sorisonsoon.common.config;

import com.sorisonsoon.common.exception.ServerInternalException;
import com.sorisonsoon.common.exception.type.ExceptionCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingConfig {
    private static final Logger logger = LoggerFactory.getLogger(EmbeddingConfig.class);

    @Value("${spring.ai.embedding.transformer.onnx.modelUri}")
    private String modelUri;

    @Value("${spring.ai.embedding.transformer.onnx.modelOutputName}")
    private String modelOutputName;

    @Value("${spring.ai.embedding.transformer.tokenizer.uri}")
    private String tokenizerUri;

    @Bean
    public TransformersEmbeddingModel embeddingModel() {
        TransformersEmbeddingModel embeddingModel = new TransformersEmbeddingModel();
        embeddingModel.setModelResource(modelUri);
        embeddingModel.setModelOutputName(modelOutputName);
        embeddingModel.setTokenizerResource(tokenizerUri);

        try {
            embeddingModel.afterPropertiesSet();
        } catch (Exception e) {
            throw new ServerInternalException(ExceptionCode.FAIL_TO_EMBEDDING_MODEL);
        }

        return embeddingModel;
    }
}
