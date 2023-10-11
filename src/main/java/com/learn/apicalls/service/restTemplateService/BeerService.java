package com.learn.apicalls.service.restTemplateService;

import com.learn.apicalls.feign.BeerFeignProxy;
import com.learn.apicalls.model.Beers;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BeerService{

    private RestTemplate restTemplate;
    private BeerFeignProxy beerFeignProxy;
    @Value("${learning.beer.beerApi}")
    private String url;
    private final String RANDOM_BEERS_URL = url;

    public BeerService(RestTemplate restTemplate, BeerFeignProxy beerFeignProxy) {
        this.restTemplate = restTemplate;
        this.beerFeignProxy = beerFeignProxy;
    }

    public List<Beers> getBeers(Integer size) {
        if(size == 1){
            throw new IllegalArgumentException("Beers API returns Beers Object instead of List<Beers>");//TODO:Implement Custom Exception
        }
        Map<String, Integer> uriVariables = new HashMap<>();
        uriVariables.put("size", size);

        ParameterizedTypeReference<List<Beers>> responseType = new ParameterizedTypeReference<List<Beers>>() {};

        String urlWithParams = UriComponentsBuilder.fromUriString(url)
                .queryParam("size", size)
                .build()
                .toUriString();

        ResponseEntity<List<Beers>> response = restTemplate.exchange(// Define the URL with URI variable
                urlWithParams,
                HttpMethod.GET,
                null,
                responseType);

        if (response.getStatusCode().is2xxSuccessful()) {
            List<Beers> beersList = response.getBody();
            return beersList;
            // Do something with the list of Beers
        }

        throw new RuntimeException("Not Completed");//TODO:Implement Custom Exception
    }

    public List<Beers> getBeersFeign(Integer size) {

        List<Beers> beer = beerFeignProxy.getBeer(size);
        return beer;
    }

    // Making 4 calls for Demonstration
    public List<Beers> getBeer(int size) {
        // Method 1: Using getForObject()
        Beers forObject = restTemplate.getForObject(url, Beers.class, size);
        System.out.println("Beers: " + forObject);

        // Method 2: Using getForEntity()
        ResponseEntity<Beers> responseEntity = restTemplate.getForEntity(url, Beers.class, size);
        Beers forEntity = responseEntity.getBody();
        System.out.println("Beers: " + forEntity);

        // Method 3: Using exchange()
        ResponseEntity<Beers> exchange = restTemplate.exchange(
                url,
                HttpMethod.GET,
                null,
                Beers.class,
                size
        );

        Beers exchangeBeer = exchange.getBody();
        System.out.println("Beers: " + exchangeBeer);

        // Method 4: Using execute() with Callback
        RequestCallback requestCallback = request -> {
            request.getHeaders().toString();
        };

        ResponseExtractor<ResponseEntity<Beers>> responseExtractor
                = restTemplate.responseEntityExtractor(Beers.class);

        ResponseEntity<Beers> forExecute = restTemplate.execute(
                url,
                HttpMethod.GET,
                requestCallback,
                responseExtractor,
                size
        );
        Beers executeBeer = forExecute.getBody();
        System.out.println("Beers: " + executeBeer);

        return List.of(executeBeer);
    }
}
