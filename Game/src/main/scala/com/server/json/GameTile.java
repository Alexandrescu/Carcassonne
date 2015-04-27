package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GameTile {
    @JsonProperty("type")
    public String type;

    @JsonProperty("sectionOwned")
    public int owned;

    public GameTile(String type, int owned) {
        this.type = type;
        this.owned = owned;
    }
}
