package app.ewen.k2hoot.model;

import android.content.Context;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import app.ewen.k2hoot.model.http.HttpManager;
import app.ewen.k2hoot.model.http.response.HttpFile;
import app.ewen.k2hoot.model.json.IJson;
import app.ewen.k2hoot.model.step.Step;

public class StepContainer extends IJson implements Parcelable {

    private static int sOccurences = 0;

    private int mId;
    private List<Step> mStepList;
    private int mCurrentIndex;

    public StepContainer(List<Step> stepList) {
        this.mId = sOccurences++;
        this.mCurrentIndex = 0;
        this.mStepList = new ArrayList<>(stepList);
        // Collections.shuffle(this.mStepList);
    }

    public void nextStep() {
        this.mCurrentIndex++;
    }

    public Step currentStep() {
        if (mCurrentIndex < mStepList.size())
            return mStepList.get(mCurrentIndex);

        return null;
    }
    public int size(){
        return mStepList.size();
    }
    public int getId() {
        return mId;
    }

    // HTTP
    public HttpFile storeInServer(Context context) {
        // Create temp file
        File file;
        try {
            file = File.createTempFile("fileName", ".json", context.getCacheDir());
            Writer writer = new FileWriter(file);
            sGson.toJson(mStepList, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        // Return token
        HttpFile httpFile = HttpManager.INSTANCE.uploadFile(file);

        // Delete temp file
        file.delete();

        return httpFile;
    }

    public static StepContainer loadFromServer(String token) {
        String jsonTxt = HttpManager.INSTANCE.loadFile(token);
        return loadFromJson(jsonTxt);
    }

    public static StepContainer loadFromJson(String jsonTxt) {
        try {
            Type stepListType = new TypeToken<ArrayList<Step>>() {}.getType();
            List<Step> l = sGson.fromJson(jsonTxt, stepListType);
            return new StepContainer(l);
        } catch (Exception e) {
            Log.e("file_debug", "Impossible to parse JSON!");
            return null;
        }
    }

    // Json
    public static StepContainer fromJson(String jsonString) {
        return fromJson(jsonString, StepContainer.class);
    }

    // Parcelable
    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected StepContainer(Parcel in) {
        mId = in.readInt();
        this.mStepList = new ArrayList<>();
        in.readParcelableList(this.mStepList, Step.class.getClassLoader());
        mCurrentIndex = in.readInt();
    }

    public static final Creator<StepContainer> CREATOR = new Creator<StepContainer>() {
        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        public StepContainer createFromParcel(Parcel in) {
            return new StepContainer(in);
        }

        @Override
        public StepContainer[] newArray(int size) {
            return new StepContainer[size];
        }
    };

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeParcelableList(mStepList, flags);
        dest.writeInt(mCurrentIndex);
    }

    @Override
    public String toString() {
        String res = "\n[\n";
        for (Step step : mStepList)
            res += step.toString() + "\n";
        res += "]";
        return res;
    }
}
