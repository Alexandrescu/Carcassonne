package com.server.json;

import com.board.Move;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameMove {
    @JsonProperty("x")
    public int x;

    @JsonProperty("y")
    public int y;

    @JsonProperty("tile")
    public GameTile tile;

    @JsonProperty("player")
    public GamePlayer player;

    @JsonProperty("direction")
    public String direction;

    // NEED TO UPDATE FOLLOWERS WHICH SHOULD BE TAKEN FROM THE BOARD

    public GameMove(String type, int x, int y, int section, GamePlayer player, String direction) {
        this.tile = new GameTile(type, section);
        this.player = player;
        this.direction = direction;
        this.x = x;
        this.y = y;
    }
}
