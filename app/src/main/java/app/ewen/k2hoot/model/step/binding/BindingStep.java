package app.ewen.k2hoot.model.step.binding;

import android.os.Parcel;
import androidx.annotation.NonNull;
import java.util.HashMap;
import java.util.Map;
import app.ewen.k2hoot.model.step.Step;

public final class BindingStep extends Step<BindingData, BindingInput> {

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

    // PARCELABLE
    protected BindingStep(Parcel in) {
        super(in);
        getData().setBindingMap(in.readHashMap(String.class.getClassLoader()));
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(getData().getBindingMap());
    }

    @NonNull
    @Override
    public String toString() {
        return "BindingStep";
    }
}
