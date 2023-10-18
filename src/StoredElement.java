import java.util.Objects;

public class StoredElement {
    private final String auth_key;
    private String data;

    public StoredElement(String data,String auth_key) {
        this.data     = data;
        this.auth_key = auth_key;
    }

    public String get_data(String key) {
        if (Objects.equals(key,auth_key)) return data;

        return null;
    }
    public Boolean set_data(String data,String key) {
        if (Objects.equals(key,auth_key)) {
            this.data = data;
            return true;
        }

        return false;
    }
}