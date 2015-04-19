package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameTileMove {
    @JsonProperty("direction")
    public String direction;

    @JsonProperty("sections")
    public List<Integer> sections;

    public GameTileMove(String direction, List<Integer> sections) {
        this.direction = direction;
        this.sections = sections;
    }
}
