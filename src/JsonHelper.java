import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.function.Function;

public class JsonHelper {
    private final JsonObject json;

    public JsonHelper(JsonObject json_data) {
        json = json_data;
    }

    public <T> T get_element(String name, Function<JsonElement,T> reader) {
        JsonElement element = json.get(name);

        if (element != null) {
            try {
                return reader.apply(element);
            } catch (Exception e) {
                System.out.println("Error reading element " + name);
            }
        }

        return null;
    }
}
