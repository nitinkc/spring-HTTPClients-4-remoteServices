package com.learn.apicalls.model;
import com.fasterxml.jackson.annotation.JsonProperty;

public record MyDto(
        @JsonProperty("id") String id,
        @JsonProperty("name") String name,
        @JsonProperty("data") MyData data,
        @JsonProperty("createdAt") String createdAt
) {
}
