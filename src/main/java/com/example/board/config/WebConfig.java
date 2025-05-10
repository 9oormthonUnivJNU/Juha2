package com.example.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    // springboot는 xml로 설정할 수 있지만 class로도 설정할 수 있다.
    private String resourcePath = "/upload/**";  // view 에서 접근할 경로
    private String savePath = "file:///Users/songjuha/study/Univ/board/springboot_img/"; // 실제 파일 저장 경로

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath)
                .addResourceLocations(savePath);
    }

    // 이렇게 <img th:src="@{|/upload/${board.storedFileName}|}" alt=""> 의 경로 접근이 가능해진다.
}
