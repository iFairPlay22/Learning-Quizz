package app.ewen.k2hoot.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
    private String mName;
    private List<Step> mStepList;
    private int mCurrentIndex;
    private int mBestScore = 0;

    public static final String SHARED_PREF_STEP_CONTAINER = "SHARED_PREF_STEP_CONTAINER";
    public static final String SHARED_PREF_STEP_CONTAINER_LIST = "SHARED_PREF_STEP_CONTAINER_LIST";
    public static final String SHARED_PREF_STEP_CONTAINER_NB = "SHARED_PREF_STEP_CONTAINER_NB";

    public StepContainer(List<Step> stepList,String name) {
        this.mId = sOccurences++;
        this.mCurrentIndex = 0;
        this.mStepList = new ArrayList<>(stepList);
        this.mName=name;
        // Collections.shuffle(this.mStepList);
    }

    public int getBestScore(){
        return mBestScore;
    }

    public void setBestScore(int score){
        mBestScore = score;
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
            List<Step>  l = generateStepsFromJson(jsonTxt);
            return new StepContainer(l,"");
        } catch (Exception e) {
            Log.e("file_debug", "Impossible to parse JSON!");
            return null;
        }
    }

    // Json
    public static StepContainer fromJson(String jsonString) {
        return fromJson(jsonString, StepContainer.class);
    }

    private static List<Step> generateStepsFromJson(String jsonString) {
        Type stepListType = new TypeToken<ArrayList<Step>>() {}.getType();
        List<Step> l = sGson.fromJson(jsonString, stepListType);
        return l;
    }

    private String writeStepsToJson(){
        return sGson.toJson(mStepList);
    }

    public void addToSharedPreferences(SharedPreferences sp){

        // Write
        int gameNb = sp.getInt(SHARED_PREF_STEP_CONTAINER_NB, 0);
        sp.edit().
                putInt(SHARED_PREF_STEP_CONTAINER_NB, gameNb+1).
                putString(SHARED_PREF_STEP_CONTAINER+ "_" + gameNb, toJson()).
                putString(SHARED_PREF_STEP_CONTAINER_LIST+ "_" + gameNb, writeStepsToJson()).apply();
        //Log.i("StepCo", SHARED_PREF_STEP_CONTAINER+ "_" + 0);
        Log.i("StepCo", sp.getString(SHARED_PREF_STEP_CONTAINER_LIST+ "_" + 0, ""));

    }

    public static List<StepContainer> readAllFromSharedPreferences(SharedPreferences sp){

        List<StepContainer> stepContainers = new ArrayList<>();
        int gameNb = sp.getInt(SHARED_PREF_STEP_CONTAINER_NB, 0);
        for (int i = 0; i < gameNb; i++) {
            Log.i("StepCo", SHARED_PREF_STEP_CONTAINER+ "_" + i);
            String jsonTxt = sp.getString(SHARED_PREF_STEP_CONTAINER + "_" + i, "");
            StepContainer sc2 = StepContainer.fromJson(jsonTxt, StepContainer.class);
            String jsonList = sp.getString(SHARED_PREF_STEP_CONTAINER_LIST + "_" + i, "");;
            sc2.mStepList = StepContainer.generateStepsFromJson(jsonList);
            //Log.i("StepCo", jsonTxt);
            Log.i("StepCo", sc2.toString());

            if (sc2 != null) stepContainers.add(sc2);
        }

        return stepContainers;
    }
    // Parcelable
    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected StepContainer(Parcel in) {
        mId = in.readInt();
        this.mStepList = new ArrayList<>();
        in.readParcelableList(this.mStepList, Step.class.getClassLoader());
        mCurrentIndex = in.readInt();
        mName = in.readString();
        mBestScore = in.readInt();
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
        dest.writeString(mName);
        dest.writeInt(mBestScore);
    }

    @Override
    public String toString() {
        String res = "\n[\n";
        for (Step step : mStepList)
            res += step.toString() + "\n";
        res += "]";
        res+= mName;
        return res;
    }

    public String getName() {
        return mName;
    }
}
