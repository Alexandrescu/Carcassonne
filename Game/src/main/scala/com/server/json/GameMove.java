package com.server.json;

import com.board.Move;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class GameMove {
    @JsonProperty("tile")
    public GameTile tile;

    @JsonProperty("player")
    public GamePlayer player;

    // NEED TO UPDATE FOLLOWERS WHICH SHOULD BE TAKEN FROM THE BOARD

    public GameMove(String type, int x, int y, int section, GamePlayer player) {
        this.tile = new GameTile(type, x, y, section, player);
        this.player = player;
    }
}
