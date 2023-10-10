package com.learn.apicalls.feign;

import com.learn.apicalls.model.Beers;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "beersApi", url = "${learning.randomData.baseurl}")
public interface BeerFeignProxy {

    @GetMapping("${learning.beer.api}")
    public List<Beers> getBeer(@RequestParam Integer size);
}

