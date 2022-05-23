package com.example.restapi_jwt_view.controller;

import com.example.restapi_jwt_view.entity.TokenDto;
import com.example.restapi_jwt_view.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class HomeController {

    @Value("${REST_API_URI}")
    private String REST_API_URL;

    @Autowired
    private RestService restService;

    private Map<String, String> errCode = Map.of("notLogin", "redirect:login",
                                                 "Access-Denied", "redirect:denied");

    @GetMapping("/")
    public String home(Model model){

        // Rest API 를 통해 데이터 로드
        ResponseEntity response = restService.process("/hello", "get", null);

        // 통신 결과 redirect 처리를 해야한다면
        if(response.getStatusCode().is3xxRedirection())
        {

            // 리다이렉트 경로로 다시 Rest API 통신
            String location = response.getHeaders().get("Location").get(0).replace(REST_API_URL, "");
            ResponseEntity redirect = restService.process(location, "get", null);

            Map<String, LinkedHashMap<String, Object>> responseMap = (Map<String, LinkedHashMap<String, Object>>) redirect.getBody();
            LinkedHashMap<String, Object> data = responseMap.get("data");

            if(data.get("errCode") != null)
                return errCode.get(data.get("errCode"));

            restService.saveSession(data);
            return "redirect:/";
        }

        Map<String, Object> responseMap = (Map<String, Object>) response.getBody();
        model.addAttribute("data", responseMap.get("data"));

        return "index";
    }


    @GetMapping("/login")
    public String loginView(){
        return "login";
    }

    @GetMapping("/register")
    public String registerView(){
        return "register";
    }

    @GetMapping("/user")
    public String userPage(){
        return "user";
    }

    @GetMapping("/admin")
    public String adminPage(){
        return "admin";
    }


    @ResponseBody
    @PostMapping("/saveSession")
    public void saveSessionForAjax(@RequestBody TokenDto token, HttpSession session){
        session.setAttribute("ACCESS-TOKEN", token.getAccessToken());
        session.setAttribute("REFRESH-TOKEN", token.getAccessToken());
    }

}
