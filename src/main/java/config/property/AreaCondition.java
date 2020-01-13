package config.property;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement
public class AreaCondition {

    private ArrayList<EvaluatedCondition> values = new ArrayList<EvaluatedCondition>();

    public AreaCondition(){

    }

    @XmlElement
    public ArrayList<EvaluatedCondition> getValues() {
        return values;
    }

    public void setValues(ArrayList<EvaluatedCondition> values) {
        this.values = values;
    }



}
