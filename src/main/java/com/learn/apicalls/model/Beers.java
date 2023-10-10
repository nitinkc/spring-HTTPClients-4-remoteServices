package com.learn.apicalls.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public record Beers(
        @JsonProperty("id") Integer id,
        @JsonProperty("uid") String uid,
        @JsonProperty("brand") String brand,
        @JsonProperty("name") String name,
        @JsonProperty("style") String style,
        @JsonProperty("hop") String hop,
        @JsonProperty("yeast") String yeast,
        @JsonProperty("malts") String malts,
        @JsonProperty("ibu") String ibu,
        @JsonProperty("alcohol") String alcohol,
        @JsonProperty("blg") String blg
) {
}
