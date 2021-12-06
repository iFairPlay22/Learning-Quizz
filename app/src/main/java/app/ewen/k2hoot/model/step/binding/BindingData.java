package app.ewen.k2hoot.model.step.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.ewen.k2hoot.model.step.IStepData;

public class BindingData implements IStepData {

    private final String subject;
    private final Map<String,String> bindingMap;

    public BindingData(Map<String,String> bind,String sub){
        bindingMap = bind;
        subject = sub;
    }
    public Map<String, String> getBindingMap() {
        return bindingMap;
    }

    public String getSubject() {
        return subject;
    }
}
