package com.dragonsoft.yqjk.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author ronin
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * 添加逻辑视图映射
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/batch-download.html")
                .setViewName("yqjk/batch-download");
        registry.addViewController("yqjk/error")
                .setViewName("yqjk/error");
    }

    /**
     * 添加拦截器拦击信息
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new InterceptorConfig()).addPathPatterns("/**/*.action")
                .excludePathPatterns("/**/*.css","/**/*.js", "/**/*.png", "/**/*.jpg",
                        "/**/*.jpeg", "/**/*.gif", "/**/fonts/*");
    }
}