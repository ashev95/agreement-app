package config.valdiator;

public enum AttributeValidatorType {
    JAVASCRIPT("javascript"),
    SELECTION("selection"),
    REGEXP("regexp");

    private String type;

    AttributeValidatorType(String type) {
        this.type = type;
    }

    String getAttributeType() {
        return type;
    }

}
