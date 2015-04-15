package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class RoomDetails {
    @JsonProperty("roomName")
    public String roomName;

    @JsonProperty("slots")
    public List<RoomSlot> slots;
}
