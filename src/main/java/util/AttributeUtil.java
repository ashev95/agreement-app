package util;

import config.attribute.Attribute;
import config.attribute.AttributesParser;
import config.attribute.FormContainer;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;

public class AttributeUtil {

    private static AttributeUtil instance;

    private FormContainer formContainer;

    private AttributeUtil(){

    }

    public static AttributeUtil getInstance() {
        if (instance == null) {
            synchronized (AttributeUtil.class) {
                instance = new AttributeUtil();
            }
        }
        return instance;
    }

    public FormContainer getFormContainer() {
        if (formContainer == null){
            loadAttributes(PropertyLoader.getInstance());
        }
        return formContainer;
    }

    private boolean loadAttributes(PropertyLoader propertyLoader) {
        InputStream inputStream = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            AttributesParser attributesParser = new AttributesParser();
            inputStream = propertyLoader.getAttributesInputStream();
            parser.parse(inputStream, attributesParser);
            formContainer = attributesParser.getFormContainer();
        } catch (Exception e) {
            e.printStackTrace();
            //logging
            return false;
        }finally {
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

    public HashMap<String, Attribute> getAttributes(String form){
        return getFormContainer().getForms().get(form).getAttributes();
    }

}
