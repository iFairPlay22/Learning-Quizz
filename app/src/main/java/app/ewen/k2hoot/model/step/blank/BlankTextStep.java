package app.ewen.k2hoot.model.step.blank;

import android.os.Parcel;
import android.os.Parcelable;

import app.ewen.k2hoot.model.step.AbstractStep;

public final class BlankTextStep extends AbstractStep<BlankTextData, BlankTextInput> {

    // GAME MANAGEMENT
    @Override
    public BlankTextData getData() {
        return new BlankTextData();
    }

    @Override
    public boolean isGoodAnswer(BlankTextInput userInput) {
        return false;
    }

    // JSON
    public static BlankTextStep fromJson(String jsonString) {
        return fromJson(jsonString, BlankTextStep.class);
    }

    // PARCELABLE
    protected BlankTextStep(Parcel in) {
        // ... = dest.writeString(...);
    }

    public static final Parcelable.Creator<BlankTextStep> CREATOR = new Parcelable.Creator<BlankTextStep>() {
        @Override
        public BlankTextStep createFromParcel(Parcel in) {
            return new BlankTextStep(in);
        }

        @Override
        public BlankTextStep[] newArray(int size) {
            return new BlankTextStep[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // dest.writeString(...);
    }
}
