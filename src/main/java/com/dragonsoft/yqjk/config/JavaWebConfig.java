package com.dragonsoft.yqjk.config;

import com.dragonsoft.authorization.servlet.LicenseManagerServlet;
import com.dragonsoft.yqjk.filter.AuthorizationFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

/**
 * @author ronin
 */
@Configuration
public class JavaWebConfig {
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
        filterRegistrationBean.setUrlPatterns(Arrays.asList("/*"));
        return filterRegistrationBean;
    }
}
