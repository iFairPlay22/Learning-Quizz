package app.ewen.k2hoot.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import app.ewen.k2hoot.model.step.question.QuestionStep;

public class QuestionBank implements Parcelable {

    private List<QuestionStep> mQuestionList;
    private int mCurrentIndex;

    public QuestionBank(List<QuestionStep> questionList) {
        this.mCurrentIndex = 0;
        this.mQuestionList = new ArrayList<>(questionList);
        Collections.shuffle(this.mQuestionList);
    }

    public QuestionStep nextQuestion() {
        this.mCurrentIndex++;;
        return getCurrentQuestion();
    }

    public QuestionStep getCurrentQuestion() {
        if (mCurrentIndex < mQuestionList.size())
            return mQuestionList.get(mCurrentIndex);

        return null;
    }

    protected QuestionBank(Parcel in) {
        mQuestionList = in.createTypedArrayList(QuestionStep.CREATOR);
        mCurrentIndex = in.readInt();
    }

    public static final Creator<QuestionBank> CREATOR = new Creator<QuestionBank>() {
        @Override
        public QuestionBank createFromParcel(Parcel in) {
            return new QuestionBank(in);
        }

        @Override
        public QuestionBank[] newArray(int size) {
            return new QuestionBank[size];
        }
    };

    @Override
    public int describeContents() {
        return this.hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(mQuestionList);
        dest.writeInt(mCurrentIndex);
    }
}
