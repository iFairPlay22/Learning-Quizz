package app.ewen.k2hoot.model.json;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Writer;
import java.util.Arrays;

import app.ewen.k2hoot.model.step.question.QuestionStep;

public abstract class IJson {

    private static final Gson sGson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .create();

    public String toJson() {
        return sGson.toJson(this);
    }

    public void toJson(Writer writer) {
        sGson.toJson(this, writer);
    }

    protected static <T extends IJson> T fromJson(String jsonString, Class<T> objectClass) {
        return sGson.fromJson(jsonString, objectClass);
    }

    public static void testJson() {
        IJson iJson = new QuestionStep("Question", Arrays.asList("Answer 1", "Answer 2", "Answer 3", "Answer 4"), 0);

        // Object => Json
        String jsonTxt = iJson.toJson();
        Log.i("json_debug", jsonTxt);

        // Json => Object
        QuestionStep qs = QuestionStep.fromJson(jsonTxt);
        Log.i("json_debug", qs.getData().getQuestion());
    }

}
