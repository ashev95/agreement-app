package config.valdiator;

import java.util.*;

public class SelectAttributeValidator extends BasicAttributeValidator {

    private HashMap<String, String> options;
    private String message;
    private String targetValue;

    public HashMap<String, String> getOptions() {
        return options;
    }

    public void setOptions(HashMap<String, String> options) {
        this.options = options;
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
        message = null;

        for (String option : options.keySet()) {
            if (targetValue.equals(option)){
                return true;
            }
        }
        message = "Значение не найдено в списке";
        return false;
    }

}
