import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;

import java.util.function.BiConsumer;

public class WSCommand {
    private final BiConsumer<WebSocket,JsonObject> code;

    public WSCommand(BiConsumer<WebSocket,JsonObject> code) {
        this.code = code;
    }

    public void run(WebSocket connection, JsonObject message) {
        code.accept(connection,message);
    }
}
