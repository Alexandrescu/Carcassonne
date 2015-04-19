package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameNextMove {
    @JsonProperty("tile")
    public String tile;

    @JsonProperty("moves")
    public GameTileMove moves;

    @JsonProperty("x")
    public int x;

    @JsonProperty("y")
    public int y;

    public GameNextMove(String tile, GameTileMove moves, int x, int y) {
        this.tile = tile;
        this.moves = moves;
        this.x = x;
        this.y = y;
    }
}
