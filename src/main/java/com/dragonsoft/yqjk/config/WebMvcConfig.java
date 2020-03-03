package com.dragonsoft.yqjk.config;

import com.dragonsoft.authorization.servlet.LicenseManagerServlet;
import com.dragonsoft.yqjk.filter.AuthorizationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

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

    /**
     * 集成许可证2.0相关的Servlet
     * @return
     */
    @Bean
    public ServletRegistrationBean licenseManagerServlet(){
        ServletRegistrationBean servletRegistrationBean =
                new ServletRegistrationBean(new LicenseManagerServlet());
        servletRegistrationBean.addUrlMappings("/license/LicenseManagerServlet");
        servletRegistrationBean.setLoadOnStartup(1);
        return servletRegistrationBean;
    }

    @Bean
    public FilterRegistrationBean authorizationFilter(){
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new AuthorizationFilter());
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/myServlet"));
        return filterRegistrationBean;
    }
}
