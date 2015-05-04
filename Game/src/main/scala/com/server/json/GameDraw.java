package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameDraw {
    @JsonProperty("tile")
    public String tile;

    @JsonProperty("slot")
    public int slot;

    public GameDraw(String tile, int slot) {
        this.tile = tile;
        this.slot = slot;
    }
}
