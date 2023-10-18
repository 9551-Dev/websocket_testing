import java.util.HashMap;

public class CommandHandler {
    public HashMap<String,WSCommand> list;

    public CommandHandler(WebsocketHandler server) {
        list = new HashMap<>();
    }
}
