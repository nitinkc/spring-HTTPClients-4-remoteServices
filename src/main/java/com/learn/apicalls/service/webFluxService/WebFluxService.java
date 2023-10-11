package com.learn.apicalls.service.webFluxService;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class WebFluxService {

    WebClient client = WebClient.create();

    public void getResponseUsingWebFlux(){
        WebClient.ResponseSpec responseSpec = client.get()
                .uri("https://random-data-api.com/api/v2/beers?size=2")
                .retrieve();

    }

}
