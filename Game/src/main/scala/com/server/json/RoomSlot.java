package com.server.json;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RoomSlot {
    @JsonProperty("slot")
    public int slot;

    @JsonProperty("playerName")
    public String playerName;

    @JsonProperty("isAI")
    public boolean isAI;

    @JsonProperty("isEmpty")
    public boolean isEmpty;

    @JsonIgnore
    public String uuid;

    @JsonIgnore
    public String token;
}
