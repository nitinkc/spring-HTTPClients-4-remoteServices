package com.learn.apicalls.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MyRequestData(
        @JsonProperty("year") Integer year,
        @JsonProperty("price") Float price,
        @JsonProperty("CPU model") String cPUmodel,
        @JsonProperty("Hard disk size") String harddisksize

) {
}
