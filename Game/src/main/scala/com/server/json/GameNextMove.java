package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class GameNextMove {
    @JsonProperty("moves")
    public Map<String, List<Integer>> moves;

    @JsonProperty("x")
    public int x;

    @JsonProperty("y")
    public int y;

    public GameNextMove(Map<String, List<Integer>> moves, int x, int y) {
        this.moves = moves;
        this.x = x;
        this.y = y;
    }
}
