package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameNextMoveList {
    @JsonProperty("moveList")
    public List<GameNextMove> moveList;

    public GameNextMoveList(List<GameNextMove> moves) {
        this.moveList = moves;
    }
}
