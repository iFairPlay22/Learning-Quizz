package app.ewen.k2hoot.model.step.binding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import app.ewen.k2hoot.model.step.IStepData;

public class BindingData implements IStepData {

    private Map<String,String> bindingMap;

    public BindingData(Map<String,String> bind){
        bindingMap = bind;
    }
    public Map<String, String> getBindingMap() {
        return bindingMap;
    }

    public void setBindingMap(Map<String, String> bindingMap) {
        this.bindingMap = bindingMap;
    }
}
