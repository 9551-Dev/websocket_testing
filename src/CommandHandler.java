import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;

import java.util.HashMap;

public class CommandHandler {

    private final HashMap<String,StoredElement> database;
    public HashMap<String,WSCommand> list;

    public CommandHandler(WebsocketHandler server) {
        this.list = new HashMap<>();
        this.database = new HashMap<>();

        list.put("echo",new WSCommand((connection,data) -> connection.send(server.gson.toJson(data))));

        list.put("add_one",new WSCommand((connection,data) -> {
            JsonHelper helper = new JsonHelper(data);
            int num = helper.get_element("num",JsonElement::getAsInt);

            data.addProperty("num",num+1);

            connection.send(server.gson.toJson(data));
        }));

        list.put("write",new WSCommand((connection,data) -> {
            JsonHelper helper = new JsonHelper(data);

            JsonObject response = new JsonObject();

            String auth      = helper.get_element("auth",JsonElement::getAsString);
            String save_data = helper.get_element("data",JsonElement::getAsString);

            String key = helper.get_element("key",JsonElement::getAsString);
            if (database.containsKey(key)) {
                StoredElement existing = database.get(key);

                Boolean success = existing.set_data(save_data,auth);
                response.addProperty("success",success);
            } else {
                StoredElement new_entry = new StoredElement(save_data,auth);

                database.put(key,new_entry);

                response.addProperty("success",true);
            }

            connection.send(server.gson.toJson(response));
        }));

        list.put("read",new WSCommand((connection,data) -> {
            JsonHelper helper   = new JsonHelper(data);
            JsonObject response = new JsonObject();

            String auth = helper.get_element("auth",JsonElement::getAsString);
            String key  = helper.get_element("key", JsonElement::getAsString);

            boolean success = false;

            if (database.containsKey(key)) {
                StoredElement requested = database.get(key);

                String read_result = requested.get_data(auth);

                if (read_result != null) {
                    success = true;
                    response.addProperty("data",read_result);
                }
            }

            response.addProperty("success",success);

            connection.send(server.gson.toJson(response));
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
