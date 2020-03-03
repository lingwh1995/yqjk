package com.dragonsoft.yqjk.filter;


import javax.servlet.*;
import java.io.IOException;

/**
 * @author ronin
 * @version V1.0
 * @since 2020/3/3 10:18
 */
public class AuthorizationFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        try {
//            // 对不过滤的请求进行判断，如果是授权相关的请求，则直接return
//            // 判断是否已授权，true是，false否，jar包中已进行了页面的跳转，
//            // 这边直接在过滤器中return
            if(!AuthorUtil.verification(servletRequest, servletResponse, filterChain)){
                return;
            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
