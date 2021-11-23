package app.ewen.k2hoot.model.step.question;

import java.util.List;

import app.ewen.k2hoot.model.step.IStepData;

public class QuestionData implements IStepData {

    protected final String mQuestion;
    protected final List<String> mPropositions;

    public QuestionData(String question, List<String> propositions) {
        mQuestion = question;
        mPropositions = propositions;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public List<String> getPropositions() {
        return mPropositions;
    }
}
