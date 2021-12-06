package app.ewen.k2hoot.model.step.binding;

import java.util.Map;

import app.ewen.k2hoot.model.step.IStepInput;

public class BindingInput implements IStepInput {

    private Map<String,String> bindingMap;

    public BindingInput(Map<String,String> bind){ bindingMap = bind;}

    public Map<String, String> getBindingMap() {
        return bindingMap;
    }
}
