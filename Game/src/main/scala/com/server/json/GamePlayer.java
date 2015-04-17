package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GamePlayer {
    @JsonProperty("slot")
    public int slot;

    @JsonProperty("token")
    public String token;
}
