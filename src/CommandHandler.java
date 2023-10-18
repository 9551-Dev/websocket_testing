import org.java_websocket.WebSocket;

import java.util.ArrayList;
import java.util.HashMap;

public class CommandHandler {
    public HashMap<String,WSCommand> list;
    private final WebsocketHandler server;

    public CommandHandler(WebsocketHandler ws_server) {
        list = new HashMap<>();
        server = ws_server;

        list.put("test",new WSCommand((connection,name) -> {
            ArrayList<String> load = new ArrayList<>();

            load.add("Hello world");

            connection.send(server.gson.toJson(load));
        }));
    }

    public Boolean command_exists(String name) {
        return list.containsKey(name);
    }

    public void run_command(String name,WebSocket connection,String message) {
        WSCommand command = list.get(name);

        command.run(connection,message);
    }
}
