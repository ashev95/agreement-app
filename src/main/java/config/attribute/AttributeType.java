package config.attribute;

public enum AttributeType {
    ID("id"),
    CLIENT("client"),
    NUMBER("number"),
    SELECT("select"),
    DATE("date"),
    TEXT("text");

    private String type;

    AttributeType(String type) {
        this.type = type;
    }

    String getAttributeType() {
        return type;
    }

}
