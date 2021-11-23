package app.ewen.k2hoot.model.step.binding;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.HashMap;
import java.util.Map;

import app.ewen.k2hoot.model.step.AbstractStep;

public final class BindingStep extends AbstractStep<BindingData, BindingInput> {

    // GAME MANAGEMENT
    @Override
    public BindingData getData() {
        return new BindingData();
    }

    @Override
    public boolean isGoodAnswer(BindingInput userInput) {
        return false;
    }

    // JSON
    public static BindingStep fromJson(String jsonString) {
        return fromJson(jsonString, BindingStep.class);
    }

    // PARCELABLE
    protected BindingStep(Parcel in) {
        // ... = dest.writeString(...);
    }

    public static final Parcelable.Creator<BindingStep> CREATOR = new Parcelable.Creator<BindingStep>() {
        @Override
        public BindingStep createFromParcel(Parcel in) {
            return new BindingStep(in);
        }

        @Override
        public BindingStep[] newArray(int size) {
            return new BindingStep[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        // dest.writeString(...);
    }
}
