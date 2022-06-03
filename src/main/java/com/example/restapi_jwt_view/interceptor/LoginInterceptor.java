package com.example.restapi_jwt_view.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        Map<String, String> cookies = getCookies(request);

        if(cookies.isEmpty())
        {
            System.err.println("[INTERCEPTOR] Login Cookie Not Found");
            response.sendRedirect("/login");
            return false;
        }
        else if(cookies.get("ACCESS-TOKEN").isEmpty())
        {
            System.err.println("[INTERCEPTOR] Login Cookie Not Found");
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }

    public Map<String, String> getCookies (HttpServletRequest request) {

        Cookie[] cookies = request.getCookies();
        Map<String, String> cookieMap = new HashMap<>();

        if(cookies !=  null ) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(),cookie.getValue());
            }
        }
        return  cookieMap;
    }

}
