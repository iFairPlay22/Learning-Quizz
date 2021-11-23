package app.ewen.k2hoot.model.step;

import android.os.Parcelable;

import app.ewen.k2hoot.model.json.IJson;

public abstract class AbstractStep<Data extends IStepData, Input extends IStepInput> extends IJson implements Parcelable {

    // GAME MANAGEMENT
    public abstract Data getData();
    public abstract boolean isGoodAnswer(Input userInput);

    // PARCELABLE
    @Override
    public int describeContents() {
        return this.hashCode();
    }

}
