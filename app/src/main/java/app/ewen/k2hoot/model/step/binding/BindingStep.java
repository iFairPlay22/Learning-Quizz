package app.ewen.k2hoot.model.step.binding;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import app.ewen.k2hoot.model.step.Step;

public final class BindingStep extends Step<BindingData, BindingInput> {

    // GAME MANAGEMENT
    @Override
    public BindingData getData() {
        return new BindingData();
    }

    @Override
    public boolean isGoodAnswer(BindingInput userInput) {
        return false;
    }

    // PARCELABLE
    protected BindingStep(Parcel in) {
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
        return "BindingStep";
    }
}
