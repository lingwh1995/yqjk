package com.dragonsoft.yqjk.filter;


import com.dragonsoft.authorization.util.AuthorUtil;
import org.json.JSONException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author ronin
 * @version V1.0
 * @since 2020/3/3 10:18
 */
public class AuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //加载许可证配置文件,根据license.enable的值判断否要进行许可证校验
        Resource resource = new ClassPathResource("author.properties");
        InputStream in = resource.getInputStream();
        Properties properties = new Properties();
        properties.load(in);
        in.close();
        //如果启动许可证,则执行下面的校验操作
        boolean licenseIsEnable = Boolean.parseBoolean(properties.get("license.enable")+"");
        if(licenseIsEnable) {
            try {
                String requestURI = ((HttpServletRequest) servletRequest).getRequestURI();
                //放行列表中包含requestURI就直接放行,不包含requestURI就就行校验后再决定是否放行
                if (!(requestURI.contains("/authorizationPage.html")
                        || requestURI.contains("/importAuthorizationFile.html")
                        || requestURI.contains("/license/LicenseManagerServlet")
                        || requestURI.contains(".css")
                        || requestURI.contains(".js")
                        || requestURI.contains(".jpg")
                        || requestURI.contains(".png")
                        || requestURI.contains(".gif"))) {
                    Boolean isValidate = AuthorUtil.verification(servletRequest, servletResponse, filterChain);
                    if (!isValidate) {
                        return;
                    }
                } else {
                    filterChain.doFilter(servletRequest, servletResponse);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            //否则不做任何处理,直接放行
            filterChain.doFilter(servletRequest,servletResponse);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
