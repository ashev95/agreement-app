package config.property;

import javax.xml.bind.annotation.*;
import java.util.*;

@XmlRootElement
public class YearOfConstruction {

    private ArrayList<EvaluatedCondition> values = new ArrayList<EvaluatedCondition>();

    public YearOfConstruction (){

    }

    @XmlElement
    public ArrayList<EvaluatedCondition> getValues() {
        return values;
    }

    public void setValues(ArrayList<EvaluatedCondition> values) {
        this.values = values;
    }

}
