package util;

import config.column.Column;
import config.column.ColumnsParser;
import config.column.ViewContainer;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ColumnUtil {

    private static ColumnUtil instance;

    private ViewContainer viewContainer;

    private ColumnUtil(){

    }

    public static ColumnUtil getInstance() {
        if (instance == null) {
            synchronized (ColumnUtil.class) {
                instance = new ColumnUtil();
            }
        }
        return instance;
    }

    public ViewContainer getViewContainer() {
        if (viewContainer == null){
            loadColumns(PropertyLoader.getInstance());
        }
        return viewContainer;
    }

    private boolean loadColumns(PropertyLoader propertyLoader) {
        InputStream inputStream = null;
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            ColumnsParser columnsParser = new ColumnsParser();
            inputStream = propertyLoader.getColumnsInputStream();
            parser.parse(inputStream, columnsParser);
            viewContainer = columnsParser.getViewContainer();
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

    public HashMap<String, Column> getColumns(String view){
        return getViewContainer().getViews().get(view).getColumns();
    }

    private Object getValue(Object object, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        Object value = field.get(object);
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

    public String getCellValue(Object object, Column column) throws ScriptException, NoSuchFieldException, IllegalAccessException {
        HashMap<String, String> result = new  HashMap<String, String>();
        Pattern r = Pattern.compile("\\{[A-Za-z0-9.]+\\}");
        Matcher m = r.matcher(column.getScript());
        while (m.find()) {
            String fieldName = m.group().substring(1, m.group().length() - 1);
            String fieldValue = null;
            if (fieldName.contains(".")){
                String [] fields = fieldName.split("\\.");
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
        String resultScript = "";
        String[] resultScriptArray = column.getScript().split("return");
        if (resultScriptArray.length > 1){
            if (resultScriptArray.length > 2){
                for (int i = 0; i < (resultScriptArray.length - 1); i++){
                    if (i > 0){
                        resultScript = resultScript + "return";
                    }
                    resultScript = resultScript + resultScriptArray[i];
                }
                resultScript = resultScript + resultScriptArray[resultScriptArray.length - 1];
            }else{
                resultScript = resultScriptArray[0] + resultScriptArray[1];
            }
        }else{
            resultScript = resultScriptArray[0];
        }

        resultScript = resultScript.replaceAll("let ", "var ");

        for (String fieldName : result.keySet()){
            resultScript = resultScript.replaceAll("\\{" + fieldName + "\\}", result.get(fieldName));
        }
        String cellValue = null;
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("JavaScript");
        cellValue = (String) engine.eval(resultScript);
        return cellValue;
    }

}
