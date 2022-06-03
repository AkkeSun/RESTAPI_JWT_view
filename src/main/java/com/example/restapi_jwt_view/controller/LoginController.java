package com.example.restapi_jwt_view.controller;

import com.example.restapi_jwt_view.entity.TokenDto;
import com.example.restapi_jwt_view.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    @Autowired
    private TokenService tokenService;


    @GetMapping("/login")
    public String loginView(){
        return "login";
    }


    @GetMapping("/logout")
    public String logout(@CookieValue(value = "ACCESS-TOKEN", required = false) String accessToken,
                         @CookieValue(value = "REFRESH-TOKEN", required = false) String refreshToken,
                         HttpServletResponse response) {

        TokenDto tokenCookie = tokenService.getTokenDto(accessToken, refreshToken);
        tokenService.logout(tokenCookie, response);
        return "redirect:/login";
    }


    @GetMapping("/register")
    public String registerView(){
        return "register";
    }


}
