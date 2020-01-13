package config.valdiator;

import java.util.ArrayList;

public class GroupValidators {

    private ArrayList<AttributeValidator> attributeValidators;
    private String attribute;
    private boolean required;

    public ArrayList<AttributeValidator> getAttributeValidators() {
        return attributeValidators;
    }

    public void setAttributeValidators(ArrayList<AttributeValidator> attributeValidators) {
        this.attributeValidators = attributeValidators;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }
}
