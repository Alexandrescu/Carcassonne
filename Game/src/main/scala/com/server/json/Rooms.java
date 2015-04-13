package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Rooms {
    private List<Room> _rooms;

    @JsonProperty("rooms")
    public List<Room> getTheName() { return _rooms; }

    public void setTheName(List<Room> rooms) { _rooms = rooms; }
}
