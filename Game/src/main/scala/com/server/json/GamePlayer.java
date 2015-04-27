package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GamePlayer {
    @JsonProperty("slot")
    public int slot;
    @JsonProperty("followers")
    public int followers;
    @JsonProperty("points")
    public int points;

    public GamePlayer(int slot, int followers, int points) {
        this.slot = slot;
        this.followers = followers;
        this.points = points;
    }
}
