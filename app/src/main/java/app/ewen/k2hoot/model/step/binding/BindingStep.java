package app.ewen.k2hoot.model.step.binding;

import android.os.Parcel;
import androidx.annotation.NonNull;
import java.util.Map;
import app.ewen.k2hoot.model.step.Step;

public final class BindingStep extends Step<BindingData, BindingInput> {

    // GAME MANAGEMENT
    private final String subject;
    private final Map<String,String> bindingMap;

    public BindingStep(String subject, Map<String,String> bindingMap){
        this.subject =subject;
        this.bindingMap = bindingMap;
    }

    @Override
    public BindingData getData() {
        return new BindingData(bindingMap, subject);
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
        bindingMap = in.readHashMap(String.class.getClassLoader());
        subject = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeMap(getData().getBindingMap());
        dest.writeString(getData().getSubject());
    }

    @NonNull
    @Override
    public String toString() {
        return "BindingStep";
    }
}
