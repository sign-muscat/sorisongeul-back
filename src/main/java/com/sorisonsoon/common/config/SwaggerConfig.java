package com.sorisonsoon.common.config;

import com.sorisonsoon.common.dto.response.ApiResponse;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Field;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("소리손순 API")
                        .version("1.0")
                        .description("소리손순 API 관리")
                );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }

    @Bean
    public OperationCustomizer operationCustomizer() {
        return (operation, handlerMethod) -> {
            this.addResponseBodyWrapperSchemaExample(operation, ApiResponse.class, "result");
            return operation;
        };
    }

    private void addResponseBodyWrapperSchemaExample(io.swagger.v3.oas.models.Operation operation, Class<?> type, String wrapFieldName) {
        final Content content = operation.getResponses().get("200").getContent();
        if (content != null) {
            content.keySet()
                    .forEach(mediaTypeKey -> {
                        final MediaType mediaType = content.get(mediaTypeKey);
                        mediaType.schema(wrapSchema(mediaType.getSchema(), type, wrapFieldName));
                    });
        }
    }

    private <T> Schema<T> wrapSchema(Schema<?> originalSchema, Class<T> type, String wrapFieldName) {
        final Schema<T> wrapperSchema = new Schema<>();
        try {
            final T instance = type.getDeclaredConstructor().newInstance();
            for (Field field : type.getDeclaredFields()) {
                field.setAccessible(true);
                wrapperSchema.addProperty(field.getName(), new Schema<>().example(field.get(instance)));
                field.setAccessible(false);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        wrapperSchema.addProperty(wrapFieldName, originalSchema);
        return wrapperSchema;
    }


}