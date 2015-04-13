package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Room {
    private String _roomName;

    @JsonProperty("roomName")
    public void setRoom(String room) {
        this._roomName = room;
    }

    public String getRoom() {
        return this._roomName;
    }
}
