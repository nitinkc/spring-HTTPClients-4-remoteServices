package com.learn.apicalls.controller;

import com.learn.apicalls.model.Beers;
import com.learn.apicalls.model.MyDto;
import com.learn.apicalls.model.MyRequestBody;
import com.learn.apicalls.service.restTemplateService.BeerService;
import com.learn.apicalls.service.restTemplateService.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class RestDemoController {
    @Autowired
    private final BeerService beerService;
    private final PostService postService;

    @GetMapping("/")
    public String health(){
        return "Health is OK!! Server running";
    }

    @GetMapping("/beers")
    public List<Beers> getBeers(@RequestParam(value = "size",required = true, defaultValue = "10") Integer size){
        if(size == 1) {
            return beerService.getBeer(1);
        }
        return beerService.getBeers(size);
    }

    @GetMapping("/beers/feign")
    public List<Beers> getBeersFeign(@RequestParam(value = "size") Integer size){
        List<Beers> beers = beerService.getBeersFeign(size);
        return beers;
    }

    @PostMapping("/post/example")
    public MyDto postData(@RequestBody MyRequestBody myRequest){
        return postService.saveData(myRequest);
    }

    @DeleteMapping("/delete/example/{id}")
    public Map<String, String> deletePostedData(@PathVariable String id){
        return postService.deleteData(id);
    }
}
