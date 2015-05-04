package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameClientPlayer {
    @JsonProperty("slot")
    public int slot;
    @JsonProperty("followers")
    public int followers;
    @JsonProperty("points")
    public int points;
    @JsonProperty("nickname")
    public String nickname;

    public GameClientPlayer(int slot, int followers, int points, String nickname) {
        this.slot = slot;
        this.followers = followers;
        this.points = points;
        this.nickname = nickname;
    }
}
