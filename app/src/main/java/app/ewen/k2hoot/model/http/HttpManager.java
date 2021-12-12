package app.ewen.k2hoot.model.http;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

import app.ewen.k2hoot.model.StepContainer;
import app.ewen.k2hoot.model.http.response.HttpFile;
import app.ewen.k2hoot.model.http.response.HttpFileList;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionStep;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpManager {

    // Singleton pattern
    private HttpManager() {}
    public static final HttpManager INSTANCE = new HttpManager();

    // Data
    private final String apiUrl = "https://file.io/";
    private final String apiKey = "KX3REVJ.P6CPCCF-KZD4WS0-N89B668-YVSGRQF"; // "VP4XQVV.DJHKKAE-M9HMPG5-GH0YYZV-FJF014Y";

    // Functions
    public HttpFile uploadFile(File file) {

        // Http request
        MediaType mediaType = MediaType.parse("text/plain");

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file",
                        file.getAbsolutePath(),
                        RequestBody.create(MediaType.parse("application/octet-stream"), file))
                .build();

        Request request = new Request.Builder()
                .url(apiUrl)
                .method("POST", body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        HashMap<String, String> r = callRequest(request);

        if (r == null) {
            Log.e("file_debug", "Error when uploading file to API!");
            return null;
        }

        Log.i("file_debug", "File " + file.getName() + " has been uploaded!");

        HttpFile httpFile = HttpFile.fromJson(r.get("json"));
        Log.i("file_debug", "Upload information : " + httpFile);

        return httpFile;
    }

    public HttpFile loadFile(String fileId) {

        Request request = new Request.Builder()
                .url(apiUrl + fileId)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        HashMap<String, String> r = callRequest(request);

        if (r == null) {
            Log.e("file_debug", "Error when loading file from API!");
            return null;
        }

        String jsonTxt = r.get("json");
        Log.i("file_debug", "File "+  fileId + " has been loaded!\n" + jsonTxt);

        return new HttpFile(jsonTxt, r.get("content"));
    }

    public boolean deleteFile(String fileId) {

        MediaType mediaType = MediaType.parse("text/plain");

        RequestBody body = RequestBody.create(mediaType, "");

        Request request = new Request.Builder()
                .url(apiUrl + fileId)
                .method("DELETE", body)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        HashMap<String, String> r = callRequest(request);

        if (r == null) {
            Log.e("file_debug", "Error when deleting file from API!");
            return false;
        }

        Log.i("file_debug", "File "+  fileId + " has been deleted!");
        return true;
    }

    public HttpFileList loadFileNameList() {

        Request request = new Request.Builder()
                .url(apiUrl)
                .method("GET", null)
                .addHeader("Authorization", "Bearer " + apiKey)
                .build();

        HashMap<String, String> r = callRequest(request);

        if (r == null) {
            Log.e("file_debug", "Error when loading file names from API!");
            return null;
        }

        HttpFileList httpFileList = HttpFileList.fromJson(r.get("json"));
        Log.i("file_debug", "File list loaded from API : " + httpFileList);

        return httpFileList;
    }

    private static class HttpAction extends AsyncTask<Request, Void, HashMap<String, String>> {

        @Override
        protected HashMap<String, String> doInBackground(Request... req) {
            OkHttpClient client = new OkHttpClient().newBuilder().build();
            try {
                Response response = client.newCall(req[0]).execute();

                HashMap<String, String> map = new HashMap<>();

                map.put("json", response.body().string());

                if (response.header("Content-Disposition", null) != null)
                    map.put("content", response.header("Content-Disposition"));

                return map;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    private HashMap<String, String> callRequest(Request request)  {
        HttpAction httpAction = new HttpAction();
        httpAction.execute(request);

        try {
            return httpAction.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
            return null;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void testFileUpload(Context context) {
        HttpManager httpManager = HttpManager.INSTANCE;

        QuestionStep q1 = new QuestionStep("Question 1?", Arrays.asList("Answer 1.1", "Answer 1.2", "Answer 1.3", "Answer 1.4"), 0);
        QuestionStep q2 = new QuestionStep("Question 2?", Arrays.asList("Answer 2.1", "Answer 2.2", "Answer 2.3", "Answer 2.4"), 1);
        StepContainer sc = new StepContainer(Arrays.asList(q1, q2),"Coucou!");

        if (false) {
            sc.storeInServer(context);
        }

        if (false) {
            httpManager.loadFileNameList();
        }

        if (false) {
            httpManager.deleteFile("BuhNPVDHWmbh");
        }

        if (false) {
            StepContainer sc2 = StepContainer.loadFromServer("I5FFKSMgRn1l");
            if (sc2 != null) Log.i("file_debug", sc2.toString());
        }
    }
}
