package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameRemoveFollower {
    @JsonProperty("x")
    public int x;

    @JsonProperty("y")
    public int y;

    @JsonProperty("section")
    public int section;

    public GameRemoveFollower(int x, int y, int section) {
        this.x = x;
        this.y = y;
        this.section = section;
    }
}
