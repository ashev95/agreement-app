package main;

import util.*;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.ArrayList;
import java.util.List;

public class ApplicationContextListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        ArrayList<String> forms = new ArrayList<String>();
        forms.add(Constant.FORM_AGREEMENT);
        forms.add(Constant.FORM_CLIENT);
        forms.add(Constant.FORM_CLIENT_EDIT);
        for (String form : forms){
            if (AttributeUtil.getInstance().getAttributes(form) == null) {
                throw new RuntimeException("Не удалось считать настройки актрибутов для формы " + form);
            }
            if (AttributeValidatorUtil.getInstance().getGroupValidators(form) == null) {
                throw new RuntimeException("Не удалось считать настройки валидаторов для формы " + form);
            }
        }

        ArrayList<String> views = new ArrayList<String>();
        views.add(Constant.VIEW_AGREEMENTS);
        views.add(Constant.VIEW_CLIENTS);

        for (String view : views){
            if (ColumnUtil.getInstance().getColumns(view) == null) {
                throw new RuntimeException("Не удалось считать настройки колонок для представления " + view);
            }
        }

    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }

}
