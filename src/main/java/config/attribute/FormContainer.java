package config.attribute;

import config.attribute.AttributeContainer;

import java.util.HashMap;

public class FormContainer {

    private HashMap<String, AttributeContainer> forms = new HashMap<String, AttributeContainer>();

    public HashMap<String, AttributeContainer> getForms() {
        return forms;
    }

    public void setForms(HashMap<String, AttributeContainer> forms) {
        this.forms = forms;
    }

}
