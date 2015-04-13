package com.server.json;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class Rooms {
    private List<Room> _rooms;

    @JsonProperty("rooms")
    public List<Room> getTheRooms() { return _rooms; }

    public void setTheRooms(List<Room> rooms) { _rooms = rooms; }
}
