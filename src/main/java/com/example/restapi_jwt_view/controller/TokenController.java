package com.example.restapi_jwt_view.controller;

import com.example.restapi_jwt_view.entity.TokenDto;
import com.example.restapi_jwt_view.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@RestController
public class TokenController {

    @Autowired
    private TokenService tokenService;


    @ResponseBody
    @PostMapping("/token")
    public void saveToken(@RequestBody TokenDto tokenDto, HttpServletResponse response) {
        tokenService.saveToken(tokenDto, response);
    }


    @ResponseBody
    @GetMapping("/token")
    public TokenDto getToken( @CookieValue(value = "ACCESS-TOKEN", required = false) String accessToken,
                              @CookieValue(value = "REFRESH-TOKEN", required = false) String refreshToken){

        return tokenService.getTokenDto(accessToken, refreshToken);
    }
}
