package com.example.restapi_jwt_view.controller;

import com.example.restapi_jwt_view.entity.TokenDto;
import com.example.restapi_jwt_view.service.TokenService;
import com.example.restapi_jwt_view.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import reactor.netty.http.server.HttpServerResponse;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class HomeController {

    @Value("${REST_API_URI}")
    private String REST_API_URL;

    @Autowired
    private RestService restService;

    @Autowired
    private TokenService tokenService;

    private Map<String, String> errCode = Map.of("notLogin", "redirect:login",
                                                 "Access-Denied", "redirect:denied");

    @GetMapping("/")
    public String home(Model model, HttpServletResponse response,
                       @CookieValue(value = "ACCESS-TOKEN", required = false) String accessToken,
                       @CookieValue(value = "REFRESH-TOKEN", required = false) String refreshToken){

        TokenDto tokenCookie = tokenService.getTokenDto(accessToken, refreshToken);

        // Rest API 를 통해 데이터 로드
        ResponseEntity restResponse = restService.process("/hello", "get", null, tokenCookie);

        // 통신 결과 redirect 처리를 해야한다면 -> Token 이 유효하지 않다면
        if(restResponse.getStatusCode().is3xxRedirection())
        {

            // 리다이렉트 경로로 다시 Rest API 통신
            String location = restResponse.getHeaders().get("Location").get(0).replace(REST_API_URL, "");
            ResponseEntity redirect = restService.process(location, "get", null, tokenCookie);
            Map<String, LinkedHashMap<String, Object>> responseMap = (Map<String, LinkedHashMap<String, Object>>) redirect.getBody();

            TokenDto token = tokenService.getTokenDto(responseMap.get("data"));

            // 에러 발생했다면 로그인으로 이동
            if(token.getErrCode() != null)
                return errCode.get(token.getErrCode());

            // 에러가 없다면 새로 생성한 토큰값을 저장하고 index 로 이동
            else
            {
                tokenService.saveToken(token, response);
                System.err.println("[NEW ACCESS TOKEN SAVED]");
                return "redirect:/";
            }
        }

        // redirect 처리를 하지 않아도 된다면 리턴
        Map<String, Object> responseMap = (Map<String, Object>) restResponse.getBody();
        model.addAttribute("data", responseMap.get("data"));

        return "index";
    }



    @GetMapping("/admin")
    public String adminPage(){
        return "admin";

    }

    @GetMapping("/denied")
    public String deniedPage(){
        return "denied";
    }

}
