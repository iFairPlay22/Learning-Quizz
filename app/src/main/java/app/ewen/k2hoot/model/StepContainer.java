package app.ewen.k2hoot.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import app.ewen.k2hoot.model.json.IJson;
import app.ewen.k2hoot.model.step.Step;

public class StepContainer extends IJson implements Parcelable {

    private List<Step> mStepList;
    private int mCurrentIndex;

    public StepContainer(List<Step> questionList) {
        this.mCurrentIndex = 0;
        this.mStepList = new ArrayList<>(questionList);
        // Collections.shuffle(this.mStepList);
    }

    public void nextStep() {
        this.mCurrentIndex++;
    }

    public Step currentStep() {
        if (mCurrentIndex < mStepList.size())
            return mStepList.get(mCurrentIndex);

        return null;
    }

    // Json
    public static StepContainer fromJson(String jsonString) {
        return fromJson(jsonString, StepContainer.class);
    }

    // Parcelable
    protected StepContainer(Parcel in) {
        mStepList = in.createTypedArrayList(Step.CREATOR);
        mCurrentIndex = in.readInt();
    }

    public static final Creator<StepContainer> CREATOR = new Creator<StepContainer>() {
        @Override
        public StepContainer createFromParcel(Parcel in) {
            return new StepContainer(in);
        }

        @Override
        public StepContainer[] newArray(int size) {
            return new StepContainer[size];
        }
    };

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mStepList);
        dest.writeInt(mCurrentIndex);
    }

    @Override
    public String toString() {
        String res = "\n[\n";
        for (Step step : mStepList)
            res += step.toString() + "\n";
        res += "]";
        return res;
    }
}
