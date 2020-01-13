package config.column;

import config.column.ColumnContainer;

import java.util.HashMap;

public class ViewContainer {

    private HashMap<String, ColumnContainer> views = new HashMap<String, ColumnContainer>();

    public HashMap<String, ColumnContainer> getViews() {
        return views;
    }

    public void setViews(HashMap<String, ColumnContainer> views) {
        this.views = views;
    }
}
