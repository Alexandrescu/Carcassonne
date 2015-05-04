package com.server.json;

import com.board.Move;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

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

    @JsonCreator
    public GameMove(@JsonProperty("x")int x, @JsonProperty("y")int y,
                    @JsonProperty("direction")String direction, @JsonProperty("owned") int owned) {
        this.x = x;
        this.y = y;
        this.direction = direction;
        this.tile = new GameTile("", owned);
    }
}
