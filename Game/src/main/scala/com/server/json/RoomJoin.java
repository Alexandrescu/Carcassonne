package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomJoin {
    @JsonProperty("roomName")
    public String roomName;

    @JsonProperty("playerName")
    public String playerName;

    @JsonProperty("slot")
    public int slot;

    @JsonProperty("isAI")
    public boolean isAI;
}
