import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;

import java.net.InetSocketAddress;

public class WebsocketHandler extends WebSocketServer {
    public final Gson gson;

    private final CommandHandler command_handler;

    public WebsocketHandler(int port) {
        super(new InetSocketAddress(port));
        System.out.println("WebSocket server is running on port " + port);

        this.command_handler = new CommandHandler(this);
        this.gson = new Gson();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onOpen(WebSocket conn, ClientHandshake handshake) {
        System.out.println("New connection from " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onClose(WebSocket conn, int code, String reason, boolean remote) {
        System.out.println("Closed connection to " + conn.getRemoteSocketAddress());
    }

    @Override
    public void onMessage(WebSocket conn, String message) {
        System.out.println("Received message: " + message);

        if (conn != null) {
            JsonObject read_array = gson.fromJson(message,JsonObject.class);

            if (read_array != null) {
                JsonElement command_type_json = read_array.get("name");

                try {
                    String command_type = command_type_json.getAsString();

                    if (command_handler.command_exists(command_type)) {
                        command_handler.run_command(command_type,conn,message);
                    }
                } catch (Exception e) {
                    System.out.println("Invalid message: " + message);
                }
            } else {
                String response_message = "invalid command " + message;
                conn.send(response_message);

                System.out.println("Sent response: " + response_message);
            }
        }
    }

    @Override
    public void onError(WebSocket conn, Exception ex) {
        System.err.println("Error occurred on connection to " + conn.getRemoteSocketAddress() + ": " + ex);
    }
}