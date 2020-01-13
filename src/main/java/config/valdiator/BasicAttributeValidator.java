package config.valdiator;

public class BasicAttributeValidator implements AttributeValidator {

    private String message;
    private AttributeValidatorType type;
    private boolean mask;

    @Override
    public boolean validate() {
        return false;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public AttributeValidatorType getType() {
        return type;
    }

    @Override
    public void setType(AttributeValidatorType type) {
        this.type = type;
    }

    @Override
    public boolean isMask() {
        return mask ;
    }

    @Override
    public void setMask(boolean mask) {
        this.mask = mask;
    }
}
