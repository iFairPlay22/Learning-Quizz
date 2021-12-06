package app.ewen.k2hoot.model.step.blank;

import java.util.List;

import app.ewen.k2hoot.model.step.IStepInput;

public class BlankTextInput implements IStepInput {
    private List<String> mAnswers;

    public BlankTextInput(List<String> answers) {
        mAnswers = answers;
    }

    public List<String> getAnswers() {
        return mAnswers;
    }
}
