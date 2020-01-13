package config.attribute;

import config.attribute.Attribute;

import java.util.*;

public class AttributeContainer {

    LinkedHashMap<String, Attribute> attributes;

    public LinkedHashMap<String, Attribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(LinkedHashMap<String, Attribute> attributes) {
        this.attributes = attributes;
    }
}
