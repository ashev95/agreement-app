package util;

import config.property.AreaCondition;
import config.property.TypeOfProperty;
import config.property.YearOfConstruction;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.IOException;
import java.io.InputStream;


public class ConfigUtil {

    private static ConfigUtil instance;

    private AreaCondition areaCondition;

    private TypeOfProperty typeOfProperty;

    private YearOfConstruction yearOfConstruction;

    private ConfigUtil(){

    }

    public static ConfigUtil getInstance() {
        if (instance == null) {
            synchronized (ConfigUtil.class) {
                instance = new ConfigUtil();
            }
        }
        return instance;
    }


    public AreaCondition getAreaCondition(PropertyLoader propertyLoader) {
        if (areaCondition == null){
            InputStream inputStream = null;
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(AreaCondition.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                inputStream = propertyLoader.getAreaConditionInputStream();
                areaCondition = (AreaCondition) jaxbUnmarshaller.unmarshal(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
                //logging
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
        }
        return areaCondition;
    }

    public TypeOfProperty getTypeOfProperty(PropertyLoader propertyLoader) {
        if (typeOfProperty == null){
            InputStream inputStream = null;
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(TypeOfProperty.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                inputStream = propertyLoader.getTypeOfPropertyInputStream();
                typeOfProperty = (TypeOfProperty) jaxbUnmarshaller.unmarshal(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
                //logging
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
        }
        return typeOfProperty;
    }

    public YearOfConstruction getYearOfConstruction(PropertyLoader propertyLoader) {
        if (yearOfConstruction == null){
            InputStream inputStream = null;
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(YearOfConstruction.class);
                Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
                inputStream = propertyLoader.getYearOfConstructionInputStream();
                yearOfConstruction = (YearOfConstruction) jaxbUnmarshaller.unmarshal(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
                //logging
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
        }
        return yearOfConstruction;
    }

}
