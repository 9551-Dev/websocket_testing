import org.java_websocket.WebSocket;

import java.util.function.BiConsumer;

public class WSCommand {
    private final BiConsumer<WebSocket,String> code;

    public WSCommand(BiConsumer<WebSocket,String> code) {
        this.code = code;
    }

    public void run(WebSocket connection,String message) {
        code.accept(connection,message);
    }
}
