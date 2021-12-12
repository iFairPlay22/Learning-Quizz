package app.ewen.k2hoot.model.http.response;

import app.ewen.k2hoot.model.json.IJson;

public class HttpFile extends IJson {
    private final String key;
    private final String name;

    public HttpFile(String k, String n) {
        this.key = k;
        this.name = n;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    // JSON
    public static HttpFile fromJson(String jsonString) {
        return fromJson(jsonString, HttpFile.class);
    }

    @Override
    public String toString() {
        return "HttpFile{" +
                "mKey='" + key + '\'' +
                ", mName='" + name + '\'' +
                '}';
    }
}
