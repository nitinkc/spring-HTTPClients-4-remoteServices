package com.learn.apicalls.controller;

import com.learn.apicalls.model.Beers;
import com.learn.apicalls.service.webFluxService.WebFluxService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

}
