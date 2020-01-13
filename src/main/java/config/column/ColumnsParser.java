package config.column;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.HashMap;
import java.util.LinkedHashMap;

public class ColumnsParser extends DefaultHandler {

    private final static String ATTR_VIEW_KEY = "key";

    private String currentElement;

    private ViewContainer viewContainer;
    private HashMap<String, ColumnContainer> views;
    private String viewKey;
    private ColumnContainer columnContainer;
    private LinkedHashMap<String, Column> columns;
    private Column column;


    public ViewContainer getViewContainer() {
        return viewContainer;
    }

    public ColumnsParser() {
        super();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        currentElement = qName;
        switch (qName){
            case "viewContainer":
                viewContainer = new ViewContainer();
                break;
            case "views":
                views = new HashMap<String, ColumnContainer>();
                viewContainer.setViews(views);
                break;
            case "view":
                viewKey = attributes.getValue(attributes.getIndex(ATTR_VIEW_KEY));
                viewContainer.getViews().put(viewKey, null);
                break;
            case "columnContainer":
                columnContainer = new ColumnContainer();
                views.put(viewKey, columnContainer);
                break;
            case "columns":
                columns = new LinkedHashMap<String, Column>();
                columnContainer.setColumns(columns);
                break;
            case "column":
                column = new Column();
                break;
        }
        super.startElement(uri, localName, qName, attributes);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (currentElement == "code" && column != null && column.getCode() != null){
            columns.put(column.getCode(), column);
        }
        currentElement = "";
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        String value = new String(ch, start, length).trim();
        if (!value.isEmpty()){
            switch (currentElement){
                case "code":
                    if (column.getCode() == null){
                        column.setCode(value);
                    }else{
                        column.setCode(column.getCode() + value);
                    }
                    break;
                case "title":
                    if (column.getTitle() == null){
                        column.setTitle(value);
                    }else{
                        column.setTitle(column.getTitle() + value);
                    }
                    break;
                case "script":
                    if (column.getScript() == null){
                        column.setScript(value);
                    }else{
                        column.setScript(column.getScript() + value);
                    }
                    break;
            }
        }

    }

}
