package com.server.json;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {
    public final String roomName;

    @JsonCreator
    public Room(@JsonProperty("roomName") String roomName) {
        this.roomName = roomName;
    }
}
