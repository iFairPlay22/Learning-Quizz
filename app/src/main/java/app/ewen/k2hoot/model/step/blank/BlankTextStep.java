package app.ewen.k2hoot.model.step.blank;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import app.ewen.k2hoot.model.step.Step;

public final class BlankTextStep extends Step<BlankTextData, BlankTextInput> {

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
        super(in);
        // ... = dest.writeString(...);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // dest.writeString(...);
    }

    @NonNull
    @Override
    public String toString() {
        return "BlankTextStep";
    }
}
