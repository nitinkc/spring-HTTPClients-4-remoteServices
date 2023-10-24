package com.learn.apicalls.service.webFluxService;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

@Service
public class WebFluxService {

    WebClient client = WebClient.create();

    public void getResponseUsingWebFlux(){
        WebClient.ResponseSpec responseSpec = client.get()
                .uri("https://random-data-api.com/api/v2/beers?size=2")
                .retrieve();

    }

    public void getPostResponseWebFlux(Map<String, Object> requestBodyFromClient) throws URISyntaxException {

        MultiValueMap<String, String> requestBodyCustom = new LinkedMultiValueMap<>();

        requestBodyCustom.add("name", "John Dow");
        requestBodyCustom.add("occupation", "timepass");

        String response = getFromReqBodyJSON(requestBodyFromClient);

        System.out.printf(response);
    }

    private String getFromReqBodyJSON(Map<String, Object> requestBodyFromClient) throws URISyntaxException {
        return client.post()
                .uri(new URI("https://httpbin.org/post"))
                .header("Authorization", "Bearer MY_SECRET_TOKEN")
                .contentType(MediaType.APPLICATION_JSON)//CHECK THIS
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBodyFromClient)//CHECK THIS
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String getFromReqBodyFORM(Map<String, Object> requestBodyFromClient) throws URISyntaxException {
        return client.post()
                .uri(new URI("https://httpbin.org/post"))
                .header("Authorization", "Bearer MY_SECRET_TOKEN")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(requestBodyFromClient.toString())//ACCEPTS STRING
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    private String getFromReqBodyLocasl(MultiValueMap<String, String> requestBodyFromJava) throws URISyntaxException {
        return client.post()
                .uri(new URI("https://httpbin.org/post"))
                .header("Authorization", "Bearer MY_SECRET_TOKEN")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromFormData(requestBodyFromJava))
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
