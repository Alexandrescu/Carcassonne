package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public class RoomJoin {
    private String _roomName;

    @JsonProperty("roomName")
    public void setRoom(String room) {
        this._roomName = room;
    }

    public String getRoom() {
        return this._roomName;
    }

    @JsonProperty("username")
    public void setUsername(String username) {
        this._username = username;
    }

    public String getUsername() {
        return this._username;
    }
    private String _username;

    @JsonProperty("userolor")
    public void setUserColor(String color) {
        this._usercolor = color;
    }

    public String getUserColor() {
        return this._usercolor;
    }

    private String _usercolor;
}
