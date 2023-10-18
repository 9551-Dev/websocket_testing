import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;

import java.util.HashMap;

public class CommandHandler {

    private final HashMap<String,String> data;
    public HashMap<String,WSCommand> list;
    private final WebsocketHandler server;

    public CommandHandler(WebsocketHandler ws_server) {
        list = new HashMap<>();
        server = ws_server;
        data = new HashMap<String,String>();

        list.put("echo",new WSCommand((connection,data) -> {
            connection.send(server.gson.toJson(data));
        }));

        list.put("add_one",new WSCommand((connection,data) -> {
            JsonHelper helper = new JsonHelper(data);
            int num = helper.get_element("num",JsonElement::getAsInt);

            data.addProperty("num",num+1);
            data.addProperty("reply",true);

            connection.send(server.gson.toJson(data));
        }));
    }

    public Boolean command_exists(String name) {
        return list.containsKey(name);
    }

    public void run_command(String name, WebSocket connection, JsonObject message) {
        WSCommand command = list.get(name);

        command.run(connection,message);
    }
}
