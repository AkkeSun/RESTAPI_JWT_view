package com.example.restapi_jwt_view.service;

import com.example.restapi_jwt_view.entity.TokenDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class RestService {

    @Value("${REST_API_URI}")
    private String REST_API_URL;


    public ResponseEntity process (String url, String type, Object obj, TokenDto token) {

        WebClient webClient = WebClient.builder()
                .baseUrl(REST_API_URL)
                .build();

        ResponseEntity responseEntity = null;

        if(type.equals("get"))
        {
            responseEntity =  webClient
                    .get()
                    .uri(url)
                    .header("ACCESS-TOKEN",  token.getAccessToken())
                    .header("REFRESH-TOKEN", token.getRefreshToken())
                    .exchange()
                    .flatMap(response -> response.toEntity(Object.class))
                    .block();
        }
        else if (type.equals("post"))
        {
            responseEntity = webClient.post().uri(url)
                    .header("ACCESS-TOKEN",  token.getAccessToken())
                    .header("REFRESH-TOKEN", token.getRefreshToken())
                    .body(BodyInserters.fromObject(obj))
                    .exchange()
                    .flatMap(response -> response.toEntity(Object.class))
                    .block();
        }
        return responseEntity;
    }


}
