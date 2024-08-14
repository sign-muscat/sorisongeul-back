package com.sorisonsoon.common.config;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class EmbeddingConfigTests {

    // EmbeddingConfig 파일에서 application.yml 파일의 설정 값을 잘 가져 오는지 테스트.
//    @Autowired
//    private EmbeddingConfig embeddingConfig;
//    @Test
//    public void testValueInjection() {
//        assertEquals("file:./onnx-output-folder/model.onnx", embeddingConfig.getModelUri());
//        assertEquals("sentence_embedding", embeddingConfig.getModelOutputName());
//        assertEquals("file:./onnx-output-folder/tokenizer.json", embeddingConfig.getTokenizerUri());
//    }

}
