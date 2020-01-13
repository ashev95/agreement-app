package util;

import config.valdiator.*;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AttributeValidatorUtil {

    private static AttributeValidatorUtil instance;

    private FormValidatorContainer formValidatorContainer;

    private AttributeValidatorUtil(){

    }

    public static AttributeValidatorUtil getInstance() {
        if (instance == null) {
            synchronized (AttributeValidatorUtil.class) {
                instance = new AttributeValidatorUtil();
            }
        }
        return instance;
    }

    public FormValidatorContainer getFormValidatorContainer() {
        if (formValidatorContainer == null){
            loadValidators(PropertyLoader.getInstance());
        }
        return formValidatorContainer;
    }

    private boolean loadValidators(PropertyLoader propertyLoader) {
        InputStream inputStream = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ValidatorsParser validatorsParser = new ValidatorsParser();
            inputStream = propertyLoader.getValidatorsInputStream();
            parser.parse(inputStream, validatorsParser);
            formValidatorContainer = validatorsParser.getFormValidatorContainer();
        } catch (Exception e) {
            e.printStackTrace();
            //logging
            return false;
        } finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    //logging
                }
            }
        }
        return true;
    }

    public HashMap<String, GroupValidators> getGroupValidators(String form){
        return getFormValidatorContainer().getForms().get(form);
    }

    public String validateEntity(Object object, String form, Set<String> attributeNames){

        HashMap<String, GroupValidators> groupValidators = getGroupValidators(form);

        for (String attributeName : attributeNames){
            try {
                Field field = object.getClass().getDeclaredField(attributeName);
                if (groupValidators.containsKey(field.getName())){
                    GroupValidators groupValidator = groupValidators.get(field.getName());
                    if (groupValidator.getAttributeValidators() != null){
                        if (groupValidator != null){
                            field.setAccessible(true);
                            Object fieldObject = field.get(object);
                            if (fieldObject != null){
                                if (fieldObject.toString().trim().isEmpty() && groupValidator.isRequired()){
                                    return "Поле '" + AttributeUtil.getInstance().getAttributes(form).get(field.getName()).getTitle() + "' обязательно для заполнения";
                                }
                                for (AttributeValidator attributeValidator : groupValidator.getAttributeValidators()){
                                    if (groupValidator.isRequired() || !attributeValidator.isMask()){
                                        boolean validate = false;
                                        if (attributeValidator instanceof JSAttributeValidator){
                                            prepareInput((JSAttributeValidator) attributeValidator, object);
                                            validate = true;
                                        }else if (attributeValidator instanceof RegExpAttributeValidator){
                                            ((RegExpAttributeValidator)attributeValidator).setTargetValue(fieldObject.toString());
                                            validate = true;
                                        }
                                        if (validate && !attributeValidator.validate()){
                                            return attributeValidator.getMessage();
                                        }
                                    }
                                }
                            }else{
                                if (groupValidator.isRequired()){
                                    return "Поле '" + AttributeUtil.getInstance().getAttributes(form).get(field.getName()).getTitle() + "' обязательно для заполнения";
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                //logging
                return "Возникла неизвестная ошибка. Обратитесь к Администратору системы";
            }
        }

        return null;
    }

    public String validateEntity(Object object, String form){
        Set<String> attributeNames = getFormValidatorContainer().getForms().get(form).keySet();
        return validateEntity(object, form, attributeNames);
    }

    private void prepareInput(JSAttributeValidator jsAttributeValidator, Object object) throws NoSuchFieldException, IllegalAccessException {
        HashMap<String, String> result = new  HashMap<String, String>();
        Pattern r = Pattern.compile("\\{[A-Za-z0-9.]+\\}");
        Matcher m = r.matcher(jsAttributeValidator.getScript());
        while (m.find()) {
            String fieldName = m.group().substring(1, m.group().length() - 1);
            String fieldValue = null;
            if (fieldName.contains(".")){
                Pattern r1 = Pattern.compile("([\\w]+.value)");
                ArrayList<String> fields = new ArrayList<String>();
                Matcher m1 = r1.matcher(fieldName);
                while (m1.find()) {
                    String f = m1.group();
                    fields.add(fields.size(), f.substring(0, f.indexOf(".")));
                }
                Object value = object;
                for (String fieldName1 : fields){
                    value = getValue(value, fieldName1);
                }
                fieldValue = value.toString();
            }else{
                fieldValue = getValueString(object, fieldName);
            }
            result.put(fieldName, fieldValue);
        }
        jsAttributeValidator.setInput(result);
    }

    private Object getValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Object value = value = field.get(object);
        return value;
    }

    private String getValueString(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Object value = getValue(object, fieldName);
        String fieldValue = "";
        if (value != null) {
            if (value instanceof Date) {
                fieldValue = Converter.getInstance().javaDateToJSDate((Date)value);
            } else {
                fieldValue = value.toString();
            }
            return fieldValue;
        }
        return null;
    }

}
