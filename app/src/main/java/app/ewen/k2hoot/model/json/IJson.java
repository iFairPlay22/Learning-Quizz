package app.ewen.k2hoot.model.json;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import app.ewen.k2hoot.model.StepContainer;
import app.ewen.k2hoot.model.step.IStepData;
import app.ewen.k2hoot.model.step.IStepInput;
import app.ewen.k2hoot.model.step.Step;
import app.ewen.k2hoot.model.step.question.QuestionStep;

public abstract class IJson {

    protected static final Gson sGson = new GsonBuilder()
                .serializeNulls()
                .disableHtmlEscaping()
                .setPrettyPrinting()
                .registerTypeAdapter(Step.class, new JsonDeserializerWithInheritance<Step<IStepData, IStepInput>>())
                .create();

    public String toJson() {
        return sGson.toJson(this);
    }

    public void toJson(Writer writer) {
        sGson.toJson(this, writer);
    }

    public static <T> T fromJson(String jsonString, Class<T> objectClass) {
        return sGson.fromJson(jsonString, objectClass);
    }
}
