package util;

import java.io.*;
import java.util.Properties;

public class PropertyLoader {

    private final static String APP_PROPERTIES_LOCATION = "app.properties";

    private final static String APP_CONFIG_FOLDER = "config";
    private final static String APP_CONFIG_ATTRIBUTES = "attributes.xml";
    private final static String APP_CONFIG_VALIDATORS = "validators.xml";
    private final static String APP_CONFIG_COLUMNS = "columns.xml";

    private final static String APP_CONFIG_AREA_CONDITION = "areaCondition.xml";
    private final static String APP_CONFIG_TYPE_OF_PROPERTY = "typeOfProperty.xml";
    private final static String APP_CONFIG_YEAR_OF_CONSTRUCTION = "yearOfConstruction.xml";

    public final static String PROPERTY_CONFIG_FOLDER = "config.folder";

    private static PropertyLoader instance;

    private Properties appProperies;

    private PropertyLoader(){

    }

    public static PropertyLoader getInstance() {
        if (instance == null) {
            synchronized (PropertyLoader.class) {
                instance = new PropertyLoader();
            }
        }
        return instance;
    }

    private Properties getAppProperties() throws IOException {
        if (appProperies == null){
            appProperies = new Properties();
            InputStream inputStream = getClass().getClassLoader().getResourceAsStream(APP_PROPERTIES_LOCATION);
            if (inputStream != null){
                appProperies.load(inputStream);
            }else{
                throw new FileNotFoundException("Property file \"" + APP_PROPERTIES_LOCATION + "\" not found");
            }
        }
        return appProperies;
    }

    public InputStream getAttributesInputStream() throws IOException {
        String file  = APP_CONFIG_FOLDER + File.separator + APP_CONFIG_ATTRIBUTES;
        String propertyConfigPath = getAppProperties().getProperty(PROPERTY_CONFIG_FOLDER);
        if (propertyConfigPath != null && !propertyConfigPath.isEmpty()){
            file = propertyConfigPath + File.separator + APP_CONFIG_ATTRIBUTES;
        }
        InputStream inputStream  = getClass().getClassLoader().getResourceAsStream(file);
        return inputStream;
    }

    public InputStream getValidatorsInputStream() throws IOException {
        String file  = APP_CONFIG_FOLDER + File.separator + APP_CONFIG_VALIDATORS;
        String propertyConfigPath = getAppProperties().getProperty(PROPERTY_CONFIG_FOLDER);
        if (propertyConfigPath != null && !propertyConfigPath.isEmpty()){
            file = propertyConfigPath + File.separator + APP_CONFIG_VALIDATORS;
        }
        InputStream inputStream  = getClass().getClassLoader().getResourceAsStream(file);
        return inputStream;
    }

    public InputStream getColumnsInputStream() throws IOException {
        String file  = APP_CONFIG_FOLDER + File.separator + APP_CONFIG_COLUMNS;
        String propertyConfigPath = getAppProperties().getProperty(PROPERTY_CONFIG_FOLDER);
        if (propertyConfigPath != null && !propertyConfigPath.isEmpty()){
            file = propertyConfigPath + File.separator + APP_CONFIG_COLUMNS;
        }
        InputStream inputStream  = getClass().getClassLoader().getResourceAsStream(file);
        return inputStream;
    }

    public InputStream getAreaConditionInputStream() throws IOException {
        String file  = APP_CONFIG_FOLDER + File.separator + APP_CONFIG_AREA_CONDITION;
        String propertyConfigPath = getAppProperties().getProperty(PROPERTY_CONFIG_FOLDER);
        if (propertyConfigPath != null && !propertyConfigPath.isEmpty()){
            file = propertyConfigPath + File.separator + APP_CONFIG_AREA_CONDITION;
        }
        InputStream inputStream  = getClass().getClassLoader().getResourceAsStream(file);
        return inputStream;
    }

    public InputStream getTypeOfPropertyInputStream() throws IOException {
        String file  = APP_CONFIG_FOLDER + File.separator + APP_CONFIG_TYPE_OF_PROPERTY;
        String propertyConfigPath = getAppProperties().getProperty(PROPERTY_CONFIG_FOLDER);
        if (propertyConfigPath != null && !propertyConfigPath.isEmpty()){
            file = propertyConfigPath + File.separator + APP_CONFIG_TYPE_OF_PROPERTY;
        }
        InputStream inputStream  = getClass().getClassLoader().getResourceAsStream(file);
        return inputStream;
    }

    public InputStream getYearOfConstructionInputStream() throws IOException {
        String file  = APP_CONFIG_FOLDER + File.separator + APP_CONFIG_YEAR_OF_CONSTRUCTION;
        String propertyConfigPath = getAppProperties().getProperty(PROPERTY_CONFIG_FOLDER);
        if (propertyConfigPath != null && !propertyConfigPath.isEmpty()){
            file = propertyConfigPath + File.separator + APP_CONFIG_YEAR_OF_CONSTRUCTION;
        }
        InputStream inputStream  = getClass().getClassLoader().getResourceAsStream(file);
        return inputStream;
    }

}