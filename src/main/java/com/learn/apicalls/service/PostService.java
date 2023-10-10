package com.learn.apicalls.service;

import com.learn.apicalls.model.MyDto;
import com.learn.apicalls.model.MyRequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;


@Service
public class PostService {

    @Autowired
    RestTemplate restTemplate;
    @Value("${learning.restApiDev.api}")
    String url;

    public MyDto saveData(MyRequestBody myRequestBody) {
        HttpHeaders httpsHeaders = new HttpHeaders();
        httpsHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        HttpEntity<Object> httpEntity = new HttpEntity<>(myRequestBody, httpsHeaders);

        MyDto result = restTemplate.exchange(url,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<MyDto>(){}).getBody();

        return result;
    }

    public Map<String, String> deleteData(String id) {
        HttpHeaders httpsHeaders = new HttpHeaders();
        httpsHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);

        ParameterizedTypeReference<Map<String, String>> responseType = new ParameterizedTypeReference<Map<String, String>>() {};

        //restTemplate.delete(url, id);

        ResponseEntity<Map<String, String>> exchange = restTemplate.exchange(url+"/{id}",
                HttpMethod.DELETE,
                null,
                responseType,
                id);

        return exchange.getBody();
    }
}
