package app.ewen.k2hoot.model.step.question;

import app.ewen.k2hoot.model.step.IStepInput;

public class QuestionInput implements IStepInput {

    protected final int mAnswerIndex;

    public QuestionInput(int answerIndex) {
        mAnswerIndex = answerIndex;
    }

    public int getAnswerIndex() {
        return mAnswerIndex;
    }
}
