package com.example.restapi_jwt_view.service;

import com.example.restapi_jwt_view.entity.TokenDto;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class TokenService {


    // 토큰을 쿠키에 저장하는 함수
    public void saveToken(TokenDto tokenDto, HttpServletResponse response) {
        setCookies("ACCESS-TOKEN", tokenDto.getAccessToken(), response);
        setCookies("REFRESH-TOKEN", tokenDto.getRefreshToken(), response);
    }


    public TokenDto getTokenDto(LinkedHashMap<String, Object> data) {
        return TokenDto.builder()
                .accessToken((String)data.get("accessToken"))
                .refreshToken((String)data.get("refreshToken"))
                .errCode((String)data.get("errCode"))
                .build();
    }

    public TokenDto getTokenDto(String accessToken, String refreshToken) {
        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // 로그아웃 (토큰 삭제)
    public void logout(TokenDto tokenDto, HttpServletResponse response) {
        deleteCookie("ACCESS-TOKEN", tokenDto.getAccessToken(), response);
        deleteCookie("REFRESH-TOKEN", tokenDto.getRefreshToken(), response);
    }


    // 쿠키를 가져오는 함수 ( servlet 스타일 )
    public Map<String, String> getCookies () {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        Cookie[] cookies = request.getCookies();
        Map<String, String> cookieMap = new HashMap<>();

        if(cookies !=  null ) {
            for (Cookie cookie : cookies) {
                cookieMap.put(cookie.getName(),cookie.getValue());
            }
        }
        System.out.println("cookieMap : " + cookieMap);
        return  cookieMap;
    }


    // 쿠키를 저장하는 함수
    public void setCookies (String key, String value, HttpServletResponse response) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(60*60*24*30);  // 유지기간 (30일)
        cookie.setPath("/");            // 쿠키 접속가능 경로 (모든 경로로 설정)
        cookie.setHttpOnly(true);       // javascript 에서 쿠키값을 읽어가지 못하도록 설정
        response.addCookie(cookie);     // 저장
    }


    // 쿠키를 삭제하는 함수
    public void deleteCookie (String key, String value, HttpServletResponse response) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(0);            // 유지기간 0 = 삭제
        response.addCookie(cookie);     // 저장
    }

}
