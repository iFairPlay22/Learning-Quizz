package app.ewen.k2hoot.model;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
import java.util.Objects;

import app.ewen.k2hoot.model.http.HttpManager;
import app.ewen.k2hoot.model.http.response.HttpFile;
import app.ewen.k2hoot.model.json.IJson;
import app.ewen.k2hoot.model.step.Step;

public class StepContainer extends IJson implements Parcelable {

    // Static field
    private static final String FILE_NAME_SEPARATOR = "__fn__";
    private static int sOccurences = 0;
    private String mSharedPrefenrecesKey;

    public StepContainer(List<Step> stepList,String name) {
        this.mId = sOccurences++;
        this.mCurrentIndex = 0;
        this.mStepList = new ArrayList<>(stepList);
        // Collections.shuffle(this.mStepList);
        this.mName = name;
    }

    // Name
    private String mName;

    public String getName() {
        return mName;
    }

    // Best score
    private int mBestScore = 0;

    public int getBestScore(){
        return mBestScore;
    }

    public void setBestScore(int score){
        mBestScore = score;
    }

    // Steps

    private List<Step> mStepList;
    private int mCurrentIndex;

    public void nextStep() {
        this.mCurrentIndex++;
    }

    public Step currentStep() {
        if (mCurrentIndex < mStepList.size())
            return mStepList.get(mCurrentIndex);

        return null;
    }

    public int stepNb(){
        return mStepList.size();
    }

    // HTTP
    public HttpFile storeInServer(Context context) {
        // Create temp file
        File file;
        try {

            file = File.createTempFile(FILE_NAME_SEPARATOR + mName + FILE_NAME_SEPARATOR, ".json", context.getCacheDir());
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
        HttpFile obj = HttpManager.INSTANCE.loadFile(token);

        return loadFromJson(obj.getName().split(FILE_NAME_SEPARATOR)[1], obj.getKey());
    }

    public static StepContainer loadFromJson(String name, String jsonTxt) {

        try {
            List<Step>  l = generateStepsFromJson(jsonTxt);
            return new StepContainer(l, name);
        } catch (Exception e) {
            Log.e("file_debug", "Impossible to parse JSON!");
            return null;
        }
    }

    // Json
    public static StepContainer fromJson(String jsonString) {
        return fromJson(jsonString, StepContainer.class);
    }

    private String writeStepsToJson(){
        return sGson.toJson(mStepList);
    }

    private static List<Step> generateStepsFromJson(String jsonString) {
        Type stepListType = new TypeToken<ArrayList<Step>>() {}.getType();
        return sGson.fromJson(jsonString, stepListType);
    }

    private static final String SHARED_PREF_STEP_CONTAINER = "SHARED_PREF_STEP_CONTAINER";
    private static final String SHARED_PREF_STEP_CONTAINER_LIST = "SHARED_PREF_STEP_CONTAINER_LIST";
    private static final String SHARED_PREF_STEP_CONTAINER_NB = "SHARED_PREF_STEP_CONTAINER_NB";

    // Shared preferences

    public void addToSharedPreferences(SharedPreferences sp){

        // Write
        int gameNb = sp.getInt(SHARED_PREF_STEP_CONTAINER_NB, 0);
        mSharedPrefenrecesKey = SHARED_PREF_STEP_CONTAINER + "_" + gameNb;
        sp.edit()
            .putInt(SHARED_PREF_STEP_CONTAINER_NB, gameNb + 1)
            .putString(SHARED_PREF_STEP_CONTAINER + "_" + gameNb, toJson())
            .putString(SHARED_PREF_STEP_CONTAINER_LIST + "_" + gameNb, writeStepsToJson())
            .apply();

    }

    public void updateSharedPreferences(SharedPreferences sp){
        sp.edit().putString(mSharedPrefenrecesKey, toJson()).apply();
    }

    public static List<StepContainer> readAllFromSharedPreferences(SharedPreferences sp){

        List<StepContainer> stepContainers = new ArrayList<>();
        int gameNb = sp.getInt(SHARED_PREF_STEP_CONTAINER_NB, 0);

        for (int i = 0; i < gameNb; i++) {
            String jsonObj = sp.getString(SHARED_PREF_STEP_CONTAINER + "_" + i, "");
            StepContainer sc = StepContainer.fromJson(jsonObj, StepContainer.class);

            String jsonList = sp.getString(SHARED_PREF_STEP_CONTAINER_LIST + "_" + i, "");
            sc.mStepList = StepContainer.generateStepsFromJson(jsonList);

            stepContainers.add(sc);

        }

        return stepContainers;
    }

    // Bundle
    public static final String BUNDLE_STATE_STEP_CONTAINER_LIST = "BUNDLE_STATE_STEP_CONTAINER_LIST";

    public static List<StepContainer> getStepContainerListFromBundle(Bundle inBundle) {
        if (inBundle == null) {
            return new ArrayList<StepContainer>();
        } else {
            return inBundle.getParcelableArrayList(BUNDLE_STATE_STEP_CONTAINER_LIST);
        }
    }

    // Parcelable

    @RequiresApi(api = Build.VERSION_CODES.Q)
    protected StepContainer(Parcel in) {
        this.mStepList = new ArrayList<>();
        in.readParcelableList(this.mStepList, Step.class.getClassLoader());
        mCurrentIndex = in.readInt();
        mName = in.readString();
        mSharedPrefenrecesKey = in.readString();
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelableList(mStepList, flags);
        dest.writeInt(mCurrentIndex);
        dest.writeString(mName);
        dest.writeString(mSharedPrefenrecesKey);
        dest.writeInt(mBestScore);
    }

    // Debug

    @Override
    public String toString() {
        String res = "\n[\n";
        for (Step step : mStepList)
            res += step.toString() + "\n";
        res += "]";
        res+= "\n Name : "+mName;
        res+= "\n id : " +mId;
        res+= "\n score : " + mBestScore;
        res+= "\n bidule : " + mSharedPrefenrecesKey;
        return res;
    }

    public void resetIndex(){
        mCurrentIndex =0;
    }

    public String getSharedPrefenrecesKey(){
        return mSharedPrefenrecesKey;
    }

    private int mId;
}
