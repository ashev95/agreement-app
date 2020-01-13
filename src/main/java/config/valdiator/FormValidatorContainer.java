package config.valdiator;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class FormValidatorContainer {

    private HashMap<String, LinkedHashMap<String, GroupValidators>> forms;

    public HashMap<String, LinkedHashMap<String, GroupValidators>> getForms() {
        return forms;
    }

    public void setForms(HashMap<String, LinkedHashMap<String, GroupValidators>> forms) {
        this.forms = forms;
    }
}
