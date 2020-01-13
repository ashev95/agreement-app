package config.valdiator;

import java.util.regex.Pattern;

public class RegExpAttributeValidator extends BasicAttributeValidator {

    private String expression;
    private String message;
    private String targetValue;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(String targetValue) {
        this.targetValue = targetValue;
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
    public boolean validate() {
        return (Pattern.compile(expression).matcher(targetValue).matches());
    }

}
