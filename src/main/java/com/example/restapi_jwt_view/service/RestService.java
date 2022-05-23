package com.example.restapi_jwt_view.service;

import com.example.restapi_jwt_view.entity.TokenDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.LinkedHashMap;

@Service
public class RestService {

    @Value("${REST_API_URI}")
    private String REST_API_URL;


    public ResponseEntity process (String url, String type, Object obj) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession();

        String accessToken  = (String) session.getAttribute("ACCESS-TOKEN");
        String refreshToken = (String) session.getAttribute("REFRESH-TOKEN");

        WebClient webClient = WebClient.builder()
                .baseUrl(REST_API_URL)
                .build();

        ResponseEntity responseEntity = null;

        if(type.equals("get"))
        {
            responseEntity =  webClient
                    .get()
                    .uri(url)
                    .header("ACCESS-TOKEN", accessToken)
                    .header("REFRESH-TOKEN", refreshToken)
                    .exchange()
                    .flatMap(response -> response.toEntity(Object.class))
                    .block();
        }
        else if (type.equals("post"))
        {
            responseEntity = webClient.post().uri(url)
                    .header("ACCESS-TOKEN", accessToken)
                    .header("REFRESH-TOKEN", refreshToken)
                    .body(BodyInserters.fromObject(obj))
                    .exchange()
                    .flatMap(response -> response.toEntity(Object.class))
                    .block();
        }
        return responseEntity;
    }


    public void saveSession(LinkedHashMap<String, Object> data) {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        HttpSession session = request.getSession();

        session.setAttribute("ACCESS-TOKEN", data.get("accessToken"));
        session.setAttribute("REFRESH-TOKEN", data.get("refreshToken"));
    }

}
