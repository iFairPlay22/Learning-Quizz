package app.ewen.k2hoot.model.step.binding;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.ewen.k2hoot.model.step.AbstractStep;

public final class BindingStep extends AbstractStep<BindingData, BindingInput> {

    // GAME MANAGEMENT

    @Override
    public BindingData getData() {
        return new BindingData(new HashMap<String, String>());
    }

    @Override
    public boolean isGoodAnswer(BindingInput userInput) {
        Map<String, String> good = getData().getBindingMap();
        for (Map.Entry<String, String> entry : userInput.getBindingMap().entrySet()) {
            if(!(good.containsKey(entry.getKey()) && good.get(entry.getKey()) == entry.getValue())){
                return false;
            }
        }
        return true;
    }

    // JSON
    public static BindingStep fromJson(String jsonString) {
        return fromJson(jsonString, BindingStep.class);
    }

    // PARCELABLE
    protected BindingStep(Parcel in) {
        getData().setBindingMap(in.readHashMap(String.class.getClassLoader()));
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
        dest.writeMap(getData().getBindingMap());
    }
}
