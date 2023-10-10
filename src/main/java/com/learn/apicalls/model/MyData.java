package com.learn.apicalls.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MyData(
        @JsonProperty("year") Integer year,
        @JsonProperty("price") Float price,
        @JsonProperty("CPU model") String cpuModel,
        @JsonProperty("Hard disk size") String harddisksize
) {
}
