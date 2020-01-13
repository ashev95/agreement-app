import controller.form.AgreementFormController;
import entity.Agreement;
import org.junit.Assert;
import org.junit.Test;

import javax.json.JsonObject;
import javax.json.JsonString;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class CalculateTest {

    AgreementFormController agreementFormController = new AgreementFormController();

    @Test
    public void calculate() throws ParseException, NoSuchFieldException, IllegalAccessException {

        double [] insuranceAmounts = {20000, 25000, 19300};
        String [] limitDateStarts = {"01.05.2015", "15.04.2016", "12.03.2019"};
        String [] limitDateEnds = {"30.06.2015", "25.09.2016", "23.09.2019"};
        String [] typesOfProperty = {"Квартира", "Дом", "Комната"};
        String [] yearsOfConstruction = {"2015", "2016", "2019"};
        String [] areas = {"54", "48", "120"};

        float [] premiums = {1360.0f, 441.71f, 411.73f};

        for (int i = 0; i < insuranceAmounts.length; i++){
            System.out.println("#" + i);
            Agreement agreement = new Agreement();
            agreement.setInsuranceAmount(insuranceAmounts[i]);
            agreement.setLimitDateStart(new SimpleDateFormat("dd.MM.yyyy").parse(limitDateStarts[i]));
            agreement.setLimitDateEnd(new SimpleDateFormat("dd.MM.yyyy").parse(limitDateEnds[i]));
            agreement.setTypeOfProperty(typesOfProperty[i]);
            agreement.setYearOfConstruction(yearsOfConstruction[i]);
            agreement.setArea(areas[i]);
            JsonObject jsonObject = agreementFormController.calculate(agreement);
            Float premium = Float.parseFloat(((JsonString)((JsonObject)jsonObject.get("premium")).get("value")).getString());
            Assert.assertEquals(premiums[i], premium.floatValue(), 2);
        }

    }

}
