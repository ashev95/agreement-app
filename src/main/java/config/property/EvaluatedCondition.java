package config.property;

public class EvaluatedCondition {

    public EvaluatedCondition(){

    }

    private String predicate;

    private Double value;

    public String getPredicate() {
        return predicate;
    }

    public void setPredicate(String predicate) {
        this.predicate = predicate;
    }

    public Double getValue() {
        return value;
    }

    public void setValue(Double value) {
        this.value = value;
    }
}
