package com.learn.apicalls.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record MyRequestBody(
        @JsonProperty("name") String name,
        @JsonProperty("data") MyRequestData data
) {
}
