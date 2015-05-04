package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;
import java.util.Map;

public class GameSlots {
    @JsonProperty("slots")
    public List<GameClientPlayer> slots;

    public GameSlots(List<GameClientPlayer> slots) {
        this.slots = slots;
    }
}
