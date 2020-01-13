package config.attribute;

import config.attribute.Attribute;

import java.util.LinkedHashMap;

public class SelectAttribute extends Attribute {

    private LinkedHashMap<String, String> options;

    public LinkedHashMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(LinkedHashMap<String, String> options) {
        this.options = options;
    }

}
