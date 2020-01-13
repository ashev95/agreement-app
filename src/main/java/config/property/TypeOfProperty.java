package config.property;

import java.util.*;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TypeOfProperty {

    private HashMap<String, Double> values = new HashMap<String, Double>();

    public TypeOfProperty(){

    }

    @XmlElement
    public HashMap<String, Double> getValues() {
        return values;
    }

    public void setValues(HashMap<String, Double> values) {
        this.values = values;
    }



}
