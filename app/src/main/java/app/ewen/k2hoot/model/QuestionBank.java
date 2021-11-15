package app.ewen.k2hoot.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionBank implements Parcelable {

    private List<Question> mQuestionList;
    private int mCurrentIndex;

    public QuestionBank(List<Question> questionList) {
        this.mCurrentIndex = 0;
        this.mQuestionList = new ArrayList<>(questionList);
        Collections.shuffle(this.mQuestionList);
    }

    public Question nextQuestion() {
        this.mCurrentIndex++;;
        return getCurrentQuestion();
    }

    public Question getCurrentQuestion() {
        if (mCurrentIndex < mQuestionList.size())
            return mQuestionList.get(mCurrentIndex);

        return null;
    }

    protected QuestionBank(Parcel in) {
        mQuestionList = in.createTypedArrayList(Question.CREATOR);
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
