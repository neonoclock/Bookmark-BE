package com.example.ktbapi.common;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI apiInfo() {
        return new OpenAPI()
                .info(new Info()
                        .title("아무말 대잔치 API 문서")
                        .description("아무말 대잔치 커뮤니티 서비스 API")
                        .version("1.0.0"));
    }

    @Bean
    public GroupedOpenApi postsApi() {
        return GroupedOpenApi.builder()
                .group("posts-controller")
                .packagesToScan("com.example.ktbapi.post.api")
                .build();
    }

    @Bean
    public GroupedOpenApi commentsApi() {
        return GroupedOpenApi.builder()
                .group("comments-controller")
                .packagesToScan("com.example.ktbapi.post.api")
                .build();
    }

    @Bean
    public GroupedOpenApi usersApi() {
        return GroupedOpenApi.builder()
                .group("users-controller")
                .packagesToScan("com.example.ktbapi.user.api")
                .build();
    }

    @Bean
    public GroupedOpenApi querydslApi() {
        return GroupedOpenApi.builder()
                .group("querydsl-controller")
                .packagesToScan("com.example.ktbapi.postquerydsl.api")
                .build();
    }
}
