package com.example.demo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SwaggerConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // OpenAPI JSON 파일 제공
        registry.addResourceHandler("/openapi3/**")
                .addResourceLocations("classpath:/static/openapi3/");

        // WebJars 리소스 제공 (커스텀 파일 우선)
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/static/webjars/", "classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // /docs 접속 시 Swagger UI로 리다이렉트
        registry.addRedirectViewController(
                "/docs",
                "/webjars/swagger-ui/5.17.14/index.html?url=/openapi3/openapi3.json"
        );

        // / 루트 접속 시 Swagger UI로 리다이렉트
        registry.addRedirectViewController(
                "/",
                "/webjars/swagger-ui/5.17.14/index.html?url=/openapi3/openapi3.json"
        );
    }
}