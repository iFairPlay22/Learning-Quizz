package app.ewen.k2hoot.model.step.blank;

import java.util.List;

import app.ewen.k2hoot.model.step.IStepData;

public class BlankTextData implements IStepData {
    //Phrase sous forme de liste de mots
    private String[] words;
    //Index dans la phrase des mots à compléter
    private List<Integer> gapIndexes;

    public BlankTextData(String[] words, List<Integer> gapIndexes) {
        this.words = words;
        this.gapIndexes = gapIndexes;
    }

    public String[] getWords() {
        return words;
    }

    public void setWords(String[] words) {
        this.words = words;
    }

    public List<Integer> getGapIndexes() {
        return gapIndexes;
    }

    public void setGapIndexes(List<Integer> gapIndexes) {
        this.gapIndexes = gapIndexes;
    }
}
