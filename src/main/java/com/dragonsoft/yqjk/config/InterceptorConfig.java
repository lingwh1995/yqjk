package com.dragonsoft.yqjk.config;

import com.dragonsoft.yqjk.service.IYqjkService;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Map;


/**
 * @author ronin
 */
public class InterceptorConfig implements HandlerInterceptor{

    @Autowired
    private IYqjkService yqjkService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (yqjkService == null) {
            BeanFactory factory = WebApplicationContextUtils.getRequiredWebApplicationContext(request.getServletContext());
            yqjkService = (IYqjkService) factory.getBean("yqjkService");
        }
        if(request.getRequestURI().equals(request.getContextPath()+"/to-yqjk-list.action")
                ||request.getRequestURI().equals(request.getContextPath()+"/batch-download.action")){
            String userId = request.getParameter("userId");
            if(userId == null) {
                String message = "对不起,您没有权限访问此系统！";
                request.setAttribute("msg",message);
                request.getRequestDispatcher("yqjk/error").forward(request,response);
                return false;
            }
            Map<String, String> userInfo = yqjkService.getUserInfo(userId);
            if(userInfo == null) {
                String message = "对不起,您没有权限访问此系统！";
                request.setAttribute("msg",message);
                request.getRequestDispatcher("yqjk/error").forward(request,response);
                return false;
            }else {
                //说明某一字段查询的时候为空,说明T_USERS中用户信息不全
                if (userInfo.size() < 4) {
                    String message = "请检查用户信息是否完整,当前用户ID:" + userId;
                    request.setAttribute("msg", message);
                    request.getRequestDispatcher("yqjk/error").forward(request, response);
                    return false;
                }
            }
            HttpSession session = request.getSession();
            session.setAttribute(userId,userInfo);
        }
        return true;
    }
}
