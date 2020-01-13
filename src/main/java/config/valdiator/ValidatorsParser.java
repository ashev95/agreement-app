package config.valdiator;

import config.attribute.SelectAttribute;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import util.AttributeUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ValidatorsParser extends DefaultHandler {

    private final static String ATTR_FORM_KEY = "key";
    private final static String ATTR_VALIDATOR_GROUP_VALIDATOR_ATTRIBUTE = "attribute";
    private final static String ATTR_VALIDATOR_GROUP_VALIDATOR_REQUIRED = "required";
    private final static String ATTR_VALIDATOR_TYPE = "type";
    private final static String ATTR_VALIDATOR_IS_MASK = "isMask";

    private FormValidatorContainer formValidatorContainer;
    private LinkedHashMap<String, GroupValidators> groupValidatorsMap;
    private String formKey;
    private GroupValidators groupValidators;
    private HashMap<String, LinkedHashMap<String, GroupValidators>> forms;
    private String attribute;
    private boolean required;
    private AttributeValidator attributeValidator;
    private String validatorType;
    private ArrayList<AttributeValidator> attributeValidators;
    private HashMap<String, String> options;
    private String isMask;

    private String currentElement;


    public FormValidatorContainer getFormValidatorContainer() {
        return formValidatorContainer;
    }

    public ValidatorsParser() {
        super();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = qName;
        switch (qName){
            case "formValidatorContainer":
                formValidatorContainer = new FormValidatorContainer();
                break;
            case "forms":
                forms = new HashMap<String, LinkedHashMap<String, GroupValidators>>();
                formValidatorContainer.setForms(forms);
                break;
            case "form":
                formKey = attributes.getValue(attributes.getIndex(ATTR_FORM_KEY));
                groupValidatorsMap = new LinkedHashMap<String, GroupValidators>();
                forms.put(formKey, groupValidatorsMap);
                break;
            case "groupValidators":
                attribute = attributes.getValue(attributes.getIndex(ATTR_VALIDATOR_GROUP_VALIDATOR_ATTRIBUTE));
                required = false;
                if (attributes.getIndex(ATTR_VALIDATOR_GROUP_VALIDATOR_REQUIRED) >= 0){
                    required = "true".equals(attributes.getValue(attributes.getIndex(ATTR_VALIDATOR_GROUP_VALIDATOR_REQUIRED)));
                }
                groupValidators = new GroupValidators();
                groupValidators.setAttribute(attribute);
                groupValidators.setRequired(required);
                groupValidatorsMap.put(attribute, groupValidators);
                break;
            case "validators":
                attributeValidators = new ArrayList<AttributeValidator>();
                groupValidators.setAttributeValidators(attributeValidators);
                break;
            case "validator":
                validatorType = attributes.getValue(attributes.getIndex(ATTR_VALIDATOR_TYPE));
                switch (validatorType){
                    case "javascript":
                        attributeValidator = new JSAttributeValidator();
                        break;
                    case "regexp":
                        attributeValidator = new RegExpAttributeValidator();
                        break;
                    case "selection":
                        attributeValidator = new SelectAttributeValidator();
                        options =((SelectAttribute) AttributeUtil.getInstance().getAttributes(formKey).get(attribute)).getOptions();
                        ((SelectAttributeValidator)attributeValidator).setOptions(options);
                        break;
                    default:
                        throw new SAXException("Unsupported Validator type: [" + validatorType + "]");
                }
                attributeValidator.setType(AttributeValidatorType.valueOf(validatorType.toUpperCase()));
                if (attributes.getIndex(ATTR_VALIDATOR_IS_MASK) >= 0){
                    isMask = attributes.getValue(attributes.getIndex(ATTR_VALIDATOR_IS_MASK));
                    attributeValidator.setMask("true".equals(isMask));
                }
                attributeValidators.add(attributeValidator);
                break;
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        currentElement = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        if (!value.isEmpty()){
            switch (currentElement){
                case "script":
                    JSAttributeValidator jsAttributeValidator = (JSAttributeValidator)attributeValidator;
                    if (jsAttributeValidator.getScript() == null){
                        jsAttributeValidator.setScript(value);
                    }else{
                        jsAttributeValidator.setScript(jsAttributeValidator.getScript() + value);
                    }
                    break;
                case "expression":
                    RegExpAttributeValidator regExpAttributeValidator = ((RegExpAttributeValidator)attributeValidator);
                    if (regExpAttributeValidator.getExpression() == null){
                        regExpAttributeValidator.setExpression(value);
                    }else{
                        regExpAttributeValidator.setExpression(regExpAttributeValidator.getExpression() + value);
                    }
                    break;
                case "message":
                    if (attributeValidator.getMessage() == null){
                        attributeValidator.setMessage(value);
                    }else{
                        attributeValidator.setMessage(attributeValidator.getMessage() + value);
                    }
                    break;
            }
        }

    }

}
