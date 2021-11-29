package app.ewen.k2hoot.model.step.blank;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import app.ewen.k2hoot.model.step.AbstractStep;

public final class BlankTextStep extends AbstractStep<BlankTextData, BlankTextInput> {

    // GAME MANAGEMENT
    //Phrase sous forme de liste de mots
    private String[] mWords;
    //Index dans la phrase des mots à compléter
    private List<Integer> mGapIndexes;

    private List<String> mAnswers;

    public BlankTextStep(String[] words, List<Integer> gapIndexes) {
        mWords = words;
        mGapIndexes = gapIndexes;
    }

    @Override
    public BlankTextData getData() {
        return new BlankTextData(mWords,mGapIndexes);
    }

    @Override
    public boolean isGoodAnswer(BlankTextInput userInput) {
        List<String> answers = userInput.getAnswers();
        for (int i = 0; i < mGapIndexes.size(); i++) {
            int posInSentence = mGapIndexes.get(i);
            if(!mWords[posInSentence].equals(answers.get(i))){
                return false;
            }
        }
        return true;
    }

    // JSON
    public static BlankTextStep fromJson(String jsonString) {
        return fromJson(jsonString, BlankTextStep.class);
    }

    // PARCELABLE
    protected BlankTextStep(Parcel in) {
        // ... = dest.writeString(...);
        mWords = in.createStringArray();
        mGapIndexes = in.readArrayList(Integer.class.getClassLoader());
        mAnswers = in.createStringArrayList();
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
        dest.writeStringArray(mWords);
        dest.writeList(mGapIndexes);
        dest.writeStringList(mAnswers);
    }
}
