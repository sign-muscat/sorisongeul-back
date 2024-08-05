package com.sorisonsoon.common.config;


import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EmbeddingConfig {

    @Bean
    public TransformersEmbeddingModel transformersEmbeddingModel() {
        return new TransformersEmbeddingModel();
    }

    @Bean(name = "customEmbeddingModel")
    public TransformersEmbeddingModel embeddingModel() throws Exception {
        TransformersEmbeddingModel embeddingModel = new TransformersEmbeddingModel();
        embeddingModel.setModelResource("classpath:models/koSentenceTransformers/onnx-output-folder/model.onnx");
        embeddingModel.setTokenizerResource("classpath:models/koSentenceTransformers/onnx-output-folder/tokenizer.json");
        embeddingModel.setModelOutputName("token_embeddings");
        embeddingModel.afterPropertiesSet();

        return embeddingModel;
    }


}
