package app.ewen.k2hoot.model.step.blank;

import android.os.Parcel;
import androidx.annotation.NonNull;
import java.util.List;

import app.ewen.k2hoot.model.step.Step;

public final class BlankTextStep extends Step<BlankTextData, BlankTextInput> {

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

    // PARCELABLE
    protected BlankTextStep(Parcel in) {
        super(in);
        mWords = in.createStringArray();
        mGapIndexes = in.readArrayList(Integer.class.getClassLoader());
        mAnswers = in.createStringArrayList();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(mWords);
        dest.writeList(mGapIndexes);
        dest.writeStringList(mAnswers);
    }

    @NonNull
    @Override
    public String toString() {
        return "BlankTextStep";
    }
}
