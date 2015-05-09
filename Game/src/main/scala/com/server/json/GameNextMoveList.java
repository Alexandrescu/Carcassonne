package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameNextMoveList {
    @JsonProperty("tile")
    public String tile;

    @JsonProperty("moveList")
    public List<GameNextMove> moveList;

    public GameNextMoveList(String tile, List<GameNextMove> moves) {
        this.tile = tile;
        this.moveList = moves;
    }
}
