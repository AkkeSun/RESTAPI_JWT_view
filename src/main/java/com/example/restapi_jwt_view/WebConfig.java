package com.example.restapi_jwt_view;

import com.example.restapi_jwt_view.interceptor.LoginInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        List<String> URL_PATTERNS = Arrays.asList("/", "/admin");

        registry.addInterceptor(new LoginInterceptor())
                .addPathPatterns(URL_PATTERNS);
    }
}
