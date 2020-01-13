package config.column;

import java.util.LinkedHashMap;

public class ColumnContainer {

    LinkedHashMap<String, Column> columns;

    public LinkedHashMap<String, Column> getColumns() {
        return columns;
    }

    public void setColumns(LinkedHashMap<String, Column> columns) {
        this.columns = columns;
    }
}
