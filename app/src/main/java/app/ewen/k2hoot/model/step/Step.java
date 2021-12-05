package app.ewen.k2hoot.model.step;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

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

    // PARCELABLE
    protected Step(Parcel in) {}
    public static final Creator<Step> CREATOR = new Creator<Step>() {
        @Override
        public Step createFromParcel(Parcel in) {
            return new Step(in);
        }

        @Override
        public Step[] newArray(int size) {
            return new Step[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {}

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
