package util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Converter {

    private static Converter instance;

    public static Converter getInstance() {
        if (instance == null) {
            synchronized (Converter.class) {
                instance = new Converter();
            }
        }
        return instance;
    }

    public String javaDateToJSDate(Date date){
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.format(date);
    }

}
