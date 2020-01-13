package util;

import config.attribute.Attribute;
import config.attribute.AttributeType;
import config.attribute.SelectAttribute;
import config.column.Column;
import config.valdiator.*;
import entity.Client;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import javax.script.ScriptException;
import java.lang.reflect.Field;
import java.util.*;

public class JsonBuilder {

    private static JsonBuilder instance;

    public static JsonBuilder getInstance() {
        if (instance == null) {
            synchronized (JsonBuilder.class) {
                instance = new JsonBuilder();
            }
        }
        return instance;
    }

    public JsonObject getForm(Object object, String form, Set<String> attributeNames) throws NoSuchFieldException, IllegalAccessException {

        HashMap<String, Attribute> attributes = AttributeUtil.getInstance().getAttributes(form);

        HashMap<String, GroupValidators> groupValidatorsMap = AttributeValidatorUtil.getInstance().getGroupValidators(form);

        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

        for (String attributeName : attributeNames){
            Attribute attribute = attributes.get(attributeName);
            JsonObjectBuilder jsonObjectBuilder1 = Json.createObjectBuilder();
            jsonObjectBuilder1.add("code", attribute.getCode());
            jsonObjectBuilder1.add("title", attribute.getTitle());
            jsonObjectBuilder1.add("view", attribute.getView());
            jsonObjectBuilder1.add("type", String.valueOf(attribute.getType()).toLowerCase());
            jsonObjectBuilder1.add("editable", attribute.isEditable());
            if (attribute.getScript() != null && !attribute.getScript().isEmpty()){
                jsonObjectBuilder1.add("script", attribute.getScript());
            }else{
                jsonObjectBuilder1.add("script", "");
            }
            if (attribute.getType().equals(AttributeType.SELECT)) {
                JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                for (String option : ((SelectAttribute) attribute).getOptions().values()) {
                    jsonArrayBuilder.add(option);
                }
                jsonObjectBuilder1.add("options", jsonArrayBuilder.build());
            }

            Field field = object.getClass().getDeclaredField(attribute.getCode());
            field.setAccessible(true);
            Object value = field.get(object);
            if (attribute.getType().equals(AttributeType.CLIENT)) {
                if (value == null){
                    value = new Client();
                }
                jsonObjectBuilder1.add("value", getForm(value, Constant.FORM_CLIENT));
            }else{
                if (value == null) {
                    jsonObjectBuilder1.add("value", "");
                } else {
                    if (value instanceof Date) {
                        jsonObjectBuilder1.add("value", Converter.getInstance().javaDateToJSDate((Date)value));
                    } else {
                        jsonObjectBuilder1.add("value", value.toString());
                    }
                }
            }

            GroupValidators groupValidators = groupValidatorsMap.get(attribute.getCode());

            if (groupValidators != null) {

                jsonObjectBuilder1.add("required", groupValidators.isRequired());

                JsonArrayBuilder jsonArrayBuilder1 = Json.createArrayBuilder();

                if (groupValidators != null){
                    if (groupValidators.getAttributeValidators() != null){
                        for (AttributeValidator attributeValidator : groupValidators.getAttributeValidators()) {
                            if (attributeValidator.getType().equals(AttributeValidatorType.REGEXP)) {
                                RegExpAttributeValidator regExpAttributeValidator = (RegExpAttributeValidator) attributeValidator;
                                jsonArrayBuilder1.add(
                                        Json.createObjectBuilder()
                                                .add("type", regExpAttributeValidator.getType().toString().toLowerCase())
                                                .add("mask", regExpAttributeValidator.isMask())
                                                .add("regexp", regExpAttributeValidator.getExpression())
                                                .add("message", regExpAttributeValidator.getMessage())
                                                .build()
                                );
                            }else if (attributeValidator.getType().equals(AttributeValidatorType.JAVASCRIPT)) {
                                JSAttributeValidator jsAttributeValidator = (JSAttributeValidator) attributeValidator;
                                jsonArrayBuilder1.add(
                                        Json.createObjectBuilder()
                                                .add("type", jsAttributeValidator.getType().toString().toLowerCase())
                                                .add("mask", jsAttributeValidator.isMask())
                                                .add("input", getInput(jsAttributeValidator))
                                                .add("script", jsAttributeValidator.getScript())
                                                .add("message", jsAttributeValidator.getMessage())
                                                .build()
                                );
                            }
                        }
                    }
                }

                jsonObjectBuilder1.add("validators", jsonArrayBuilder1.build());

            } else {

                jsonObjectBuilder1.add("required", false);

            }

            jsonObjectBuilder.add(attribute.getCode(), jsonObjectBuilder1.build());
        }

        return jsonObjectBuilder.build();

    }

    private JsonObject getInput(JSAttributeValidator jsAttributeValidator){
        JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();
        if (jsAttributeValidator.getInput() != null){
            for (String fieldName : jsAttributeValidator.getInput().keySet()){
                jsonObjectBuilder.add(fieldName, jsAttributeValidator.getInput().get(fieldName));
            }
        }
        return jsonObjectBuilder.build();
    }

    public JsonObject getForm(Object object, String form) throws NoSuchFieldException, IllegalAccessException {

        HashMap<String, Attribute> attributes = AttributeUtil.getInstance().getAttributes(form);

        return getForm(object, form, attributes.keySet());
    }

    public JsonObject getView(List objects, HashMap<String, Column> columns) throws NoSuchFieldException, IllegalAccessException, ScriptException {

        JsonArrayBuilder jsonArrayBuilderHead = Json.createArrayBuilder();

        JsonArrayBuilder jsonArrayBuilderData = Json.createArrayBuilder();

        for (Column column : columns.values()) {

            jsonArrayBuilderHead.add(
                    Json.createObjectBuilder()
                            .add("code", column.getCode())
                            .add("title", column.getTitle())
                            .build()
            );

        }

        for (Object object: objects){

            JsonObjectBuilder jsonObjectBuilder = Json.createObjectBuilder();

            for (Column column : columns.values()){

                JsonObjectBuilder jsonObjectBuilder1 = Json.createObjectBuilder();;

                jsonObjectBuilder1.add("code", column.getCode());
                jsonObjectBuilder1.add("value", ColumnUtil.getInstance().getCellValue(object, column));

                jsonObjectBuilder.add(column.getCode(), jsonObjectBuilder1);

            }

            jsonArrayBuilderData.add(jsonObjectBuilder.build());

        }

        JsonObject value = Json.createObjectBuilder()
                .add("head", jsonArrayBuilderHead.build())
                .add("data", jsonArrayBuilderData.build())
                .build();
        return value;

    }

    public JsonObject getView(List objects, String view) throws IllegalAccessException, NoSuchFieldException, ScriptException {

        HashMap<String, Column> columns = ColumnUtil.getInstance().getColumns(view);

        return getView(objects, columns);
    }

}
