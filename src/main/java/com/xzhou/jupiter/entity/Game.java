package com.xzhou.jupiter.entity;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

//below program is a pratice of builder pattern
@JsonIgnoreProperties(ignoreUnknown = true)
// @JsonIgnoreProperties(ignoreUnknown = true) indicates that other fields in the
// response can be safely ignored. Without this, you’ll get an exception at runtime.

@JsonInclude(JsonInclude.Include.NON_NULL)
//@JsonInclude(JsonInclude.Include.NON_NULL) indicates that null fields can be skipped and not included.

@JsonDeserialize(builder = Game.Builder.class)
//@JsonDeserialize indicates that Jackson needs to use Game.Builder when constructing a Game object
// from JSON strings.
public class Game {
    //add below fields to match twitch response
    @JsonProperty("id")
    private final String id;

    @JsonProperty("name")
    private String name;

    @JsonProperty("box_art_url")
    private final String boxArtUrl;


    //constructor
    private Game(Builder builder) {
        this.id = builder.id;
        this.name = builder.name;
        this.boxArtUrl = builder.boxArtUrl;
    }

    //add getter for the private fields
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getBoxArtUrl() {
        return boxArtUrl;
    }

    //the Builder class must be static
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Builder {
        @JsonProperty("id")
        private String id;

        @JsonProperty("name")
        private String name;

        @JsonProperty("box_art_url")
        private String boxArtUrl;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder boxArtUrl(String boxArtUrl) {
            this.boxArtUrl = boxArtUrl;
            return this;
        }

        public Game build() {
            return new Game(this);
        }
    }

}
