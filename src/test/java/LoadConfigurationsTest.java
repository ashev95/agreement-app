import config.property.AreaCondition;
import config.property.TypeOfProperty;
import config.property.YearOfConstruction;
import util.*;
import org.junit.Assert;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;

public class LoadConfigurationsTest {

    @Test
    public void loadAttributes() {
        ArrayList<String> forms = new ArrayList<String>();
        forms.add(Constant.FORM_AGREEMENT);
        forms.add(Constant.FORM_CLIENT);
        forms.add(Constant.FORM_CLIENT_EDIT);
        for (String form : forms){
            Assert.assertNotNull(AttributeUtil.getInstance().getAttributes(form));
        }
    }

    @Test
    public void loadValidators() {
        ArrayList<String> forms = new ArrayList<String>();
        forms.add(Constant.FORM_AGREEMENT);
        forms.add(Constant.FORM_CLIENT);
        forms.add(Constant.FORM_CLIENT_EDIT);
        for (String form : forms){
            Assert.assertNotNull(AttributeValidatorUtil.getInstance().getGroupValidators(form));
        }
    }

    @Test
    public void loadColumns() {
        ArrayList<String> views = new ArrayList<String>();
        views.add(Constant.VIEW_AGREEMENTS);
        views.add(Constant.VIEW_CLIENTS);

        for (String view : views){
            Assert.assertNotNull(ColumnUtil.getInstance().getColumns(view));
        }
    }

    @Test
    public void loadConfigAreaCondition() {
        AreaCondition areaCondition = ConfigUtil.getInstance().getAreaCondition(PropertyLoader.getInstance());
        Assert.assertNotNull(areaCondition);
    }

    @Test
    public void loadConfigTypeOfProperty() {
        TypeOfProperty typeOfProperty = ConfigUtil.getInstance().getTypeOfProperty(PropertyLoader.getInstance());
        Assert.assertNotNull(typeOfProperty);
    }

    @Test
    public void loadConfigYearOfConstruction() {
        YearOfConstruction yearOfConstruction = ConfigUtil.getInstance().getYearOfConstruction(PropertyLoader.getInstance());
        Assert.assertNotNull(yearOfConstruction);
    }

}
