package app.ewen.k2hoot.model.http.response;

import java.util.List;

import app.ewen.k2hoot.model.json.IJson;

public class HttpFileList extends IJson {
    private List<HttpFile> files;

    public String getKey(String id) {
        for (HttpFile httpFile: files)
            if (httpFile.getName().equals(id))
                return httpFile.getKey();
        return null;
    }

    // JSON
    public static HttpFileList fromJson(String jsonString) {
        return fromJson(jsonString, HttpFileList.class);
    }

    @Override
    public String toString() {
        return "HttpFileList{" +
                "mFiles=" + files +
                '}';
    }
}
