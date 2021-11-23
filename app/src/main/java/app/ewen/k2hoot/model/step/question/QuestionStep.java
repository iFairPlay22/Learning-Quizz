package app.ewen.k2hoot.model.step.question;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

import app.ewen.k2hoot.model.step.AbstractStep;

public final class QuestionStep extends AbstractStep<QuestionData, QuestionInput> {

    // GAME MANAGEMENT
    private final String mQuestion;
    private final List<String> mChoiceList;
    private final int mAnswerIndex;

    public final static String DATA_QUESTION = "question";
    public final static String DATA_CHOICES_NUMBER = "choices_number";
    public final static String DATA_CHOICES_LIST = "choices_";

    public final static String INPUT_PROPOSITION_INDEX = "answer_index";

    public QuestionStep(String mQuestion, List<String> mChoiceList, int mAnswerIndex) {
        this.mQuestion = mQuestion;
        this.mChoiceList = mChoiceList;
        this.mAnswerIndex = mAnswerIndex;
    }

    @Override
    public QuestionData getData() {
        return new QuestionData(mQuestion, mChoiceList);
    }

    @Override
    public boolean isGoodAnswer(QuestionInput userInput) {
        return mAnswerIndex == userInput.getAnswerIndex();
    }

    // JSON
    public static QuestionStep fromJson(String jsonString) {
        return fromJson(jsonString, QuestionStep.class);
    }

    // PARCELABLE
    protected QuestionStep(Parcel in) {
        mQuestion = in.readString();
        mChoiceList = in.createStringArrayList();
        mAnswerIndex = in.readInt();
    }

    public static final Parcelable.Creator<QuestionStep> CREATOR = new Parcelable.Creator<QuestionStep>() {
        @Override
        public QuestionStep createFromParcel(Parcel in) {
            return new QuestionStep(in);
        }

        @Override
        public QuestionStep[] newArray(int size) {
            return new QuestionStep[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mQuestion);
        dest.writeStringList(mChoiceList);
        dest.writeInt(mAnswerIndex);
    }
}
