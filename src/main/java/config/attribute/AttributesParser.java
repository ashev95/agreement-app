package config.attribute;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class AttributesParser extends DefaultHandler {

    private final static String ATTR_FORM_KEY = "key";
    private final static String ATTR_ATTRIBUTE_EXTENDS = "class";

    private final static String ATTR_ATTRIBUTE_CLASS_ATTRIBUTE = "Attribute";
    private final static String ATTR_ATTRIBUTE_CLASS_SELECT_ATTRIBUTE = "SelectAttribute";

    private String currentElement;

    private FormContainer formContainer;
    private HashMap<String, AttributeContainer> forms;
    private String formKey;
    private AttributeContainer attributeContainer;
    private LinkedHashMap<String, Attribute> attributes1;
    private Attribute attribute;
    private LinkedHashMap<String, String> options;
    private String attributeClass;


    public FormContainer getFormContainer() {
        return formContainer;
    }

    public AttributesParser() {
        super();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = qName;
        switch (qName){
            case "formContainer":
                formContainer = new FormContainer();
                break;
            case "forms":
                forms = new HashMap<String, AttributeContainer>();
                formContainer.setForms(forms);
                break;
            case "form":
                formKey = attributes.getValue(attributes.getIndex(ATTR_FORM_KEY));
                formContainer.getForms().put(formKey, null);
                break;
            case "attributeContainer":
                attributeContainer = new AttributeContainer();
                forms.put(formKey, attributeContainer);
                break;
            case "attributes":
                attributes1 = new LinkedHashMap<String, Attribute>();
                attributeContainer.setAttributes(attributes1);
                break;
            case "attribute":
                attributeClass = ATTR_ATTRIBUTE_CLASS_ATTRIBUTE;
                attribute = new Attribute();
                if (attributes.getIndex(ATTR_ATTRIBUTE_EXTENDS) >= 0){
                    if ("SelectAttribute".equals(attributes.getValue(attributes.getIndex(ATTR_ATTRIBUTE_EXTENDS)))){
                        attributeClass = ATTR_ATTRIBUTE_CLASS_SELECT_ATTRIBUTE;
                        attribute = new SelectAttribute();
                    }
                }
                break;
            case "options":
                options = new LinkedHashMap<String, String>(1 << 4, 0.75f, true);
                ((SelectAttribute)attribute).setOptions(options);
                break;
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentElement == "code" && attribute != null && attribute.getCode() != null){
            attributes1.put(attribute.getCode(), attribute);
        }
        currentElement = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        if (!value.isEmpty()){
            switch (currentElement){
                case "code":
                    if (attribute.getCode() == null){
                        attribute.setCode(value);
                    }else{
                        attribute.setCode(attribute.getCode() + value);
                    }
                    break;
                case "title":
                    if (attribute.getTitle() == null){
                        attribute.setTitle(value);
                    }else{
                        attribute.setTitle(attribute.getTitle() + value);
                    }
                    break;
                case "view":
                    if (attribute.getView() == null){
                        attribute.setView(value);
                    }else{
                        attribute.setView(attribute.getView() + value);
                    }
                    break;
                case "type":
                    attribute.setType(AttributeType.valueOf(value.toUpperCase()));
                    if (AttributeType.valueOf(value.toUpperCase()).equals(AttributeType.SELECT) &&
                            !attributeClass.equals(ATTR_ATTRIBUTE_CLASS_SELECT_ATTRIBUTE)){
                        throw new IllegalArgumentException("Ошибка чтения файла. Атрибут с типом: [select] " +
                                "обязательно должен быть классом: [" + ATTR_ATTRIBUTE_CLASS_SELECT_ATTRIBUTE + "]");
                    }
                    break;
                case "editable":
                    attribute.setEditable("true".equals(value));
                    break;
                case "script":
                    if (attribute.getScript() == null){
                        attribute.setScript(value);
                    }else{
                        attribute.setScript(attribute.getScript() + value);
                    }
                    break;
                case "option":
                    ((SelectAttribute)attribute).getOptions().put(value, value);
                    break;
            }
        }

    }

}
