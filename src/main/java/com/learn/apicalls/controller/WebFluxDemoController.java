package com.learn.apicalls.controller;

import com.learn.apicalls.model.Beers;
import com.learn.apicalls.service.webFluxService.WebFluxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
public class WebFluxDemoController {

    private WebFluxService webFluxService;

    public WebFluxDemoController(WebFluxService webFluxService) {
        this.webFluxService = webFluxService;
    }

    @GetMapping("/beers/web-flux")
    public void getBeersWebFlux(@RequestParam(value = "size",required = true, defaultValue = "10") Integer size){
        webFluxService.getResponseUsingWebFlux();
        return ;
    }

    @PostMapping("/post/test")
    public void getData(@RequestBody Map<String, Object> requestBody){
        try {
            webFluxService.getPostResponseWebFlux(requestBody);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

}
