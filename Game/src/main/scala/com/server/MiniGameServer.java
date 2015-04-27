package com.server;

import com.corundumstudio.socketio.Configuration;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.SocketIOServer;
import com.corundumstudio.socketio.annotation.OnConnect;
import com.server.json.GameMove;

public class MiniGameServer {
    final SocketIOServer server;

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop();
    }

    public class MyEvents {
        @OnConnect
        public void onConnect(SocketIOClient client) {
            client.sendEvent("gameMove", new GameMove("D", 0, 0, -1, null, "UP"));
            client.sendEvent("gameMove", new GameMove("D", 1, 0, 1, null, "DOWN"));
            client.sendEvent("gameMove", new GameMove("D", 1, 1, 2, null, "DOWN"));
        }
    }

    public MiniGameServer() {
        Configuration config = new Configuration();
        //config.setHostname("localhost");
        config.setPort(1337);

        server = new SocketIOServer(config);

        server.addListeners(new MyEvents());
    }

}
