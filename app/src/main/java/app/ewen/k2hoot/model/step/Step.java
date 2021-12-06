package app.ewen.k2hoot.model.step;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import app.ewen.k2hoot.model.StepContainer;
import app.ewen.k2hoot.model.json.IJson;
import app.ewen.k2hoot.model.step.question.QuestionStep;

public class Step<Data extends IStepData, Input extends IStepInput> extends IJson implements Parcelable {

    // GAME MANAGEMENT
    protected Step() {}

    public Data getData() {
        return null;
    }

    public boolean isGoodAnswer(Input userInput) {
        return true;
    }

    // JSON
    @SerializedName("type")
    private String mTypeName = getClass().getName();

    public static Step fromJson(String jsonString) {
        return fromJson(jsonString, Step.class);
    }

    // PARCELABLE
    protected Step(Parcel in) {
        mTypeName = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mTypeName);
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return "Step";
    }
}
