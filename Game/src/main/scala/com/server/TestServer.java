package com.server;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOServer;
import com.server.events.GameEvents;
import com.server.json.RoomDetails;
import com.server.json.RoomSlot;

import java.util.ArrayList;

public class TestServer {
    final SocketIOServer server;

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop();
    }


    public TestServer() {
        Configuration config = new Configuration();
        //config.setHostname("localhost");
        config.setPort(1337);

        server = new SocketIOServer(config);

        RoomSlot slot = new RoomSlot();
        slot.isAI = false;
        slot.isEmpty = false;
        slot.playerName = "Andrei";
        slot.token = "token0";
        slot.slot = 0;
        slot.uuid = "";

        RoomSlot slot2 = new RoomSlot();
        slot2.isAI = false;
        slot2.isEmpty = false;
        slot2.playerName = "Alina";
        slot2.token = "token1";
        slot2.slot = 1;
        slot2.uuid = "";

        RoomDetails details = new RoomDetails();
        details.slots = new ArrayList<RoomSlot>() {{ add(slot); add(slot2); }};

        PlayerState state = new PlayerState(details);
        server.addListeners(new GameEvents(state, "MiniTest"));
    }

}
