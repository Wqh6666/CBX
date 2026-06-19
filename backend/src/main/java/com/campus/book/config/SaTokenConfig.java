package com.campus.book.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class SaTokenConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 用户端接口拦截
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/user/v1/**")
                .excludePathPatterns(
                        "/user/v1/auth/login",
                        "/user/v1/auth/send-code",
                        "/user/v1/categories",
                        "/user/v1/books"
                );

        // 管理端接口拦截
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/admin/v1/**")
                .excludePathPatterns(
                        "/admin/v1/auth/login"
                );
    }
}
