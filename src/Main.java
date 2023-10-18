public class Main {
    public static void main(String[] args) {
        int port = 8080;

        WebsocketHandler ws_handle = new WebsocketHandler(port);
        ws_handle.start();
    }
}
