package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameTile {
    @JsonProperty("type")
    public String type;

    @JsonProperty("x")
    public int x;

    @JsonProperty("y")
    public int y;

    @JsonProperty("sectionOwned")
    public int owned;

    @JsonProperty("player")
    public GamePlayer player;

    public GameTile(String type, int x, int y, int owned, GamePlayer player) {
        this.type = type;
        this.x = x;
        this.y = y;
        this.owned = owned;
        this.player = player;
    }
}
