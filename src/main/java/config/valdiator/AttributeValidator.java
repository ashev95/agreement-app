package config.valdiator;

public interface AttributeValidator {

    boolean validate();

    public String getMessage();

    public void setMessage(String message);

    public AttributeValidatorType getType();

    public void setType(AttributeValidatorType type);

    public boolean isMask();

    public void setMask(boolean mask);

}
