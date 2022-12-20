package br.buss.fincontrolapp.helpers;

import java.util.Calendar;

public class DBUtils {

    public DBUtils() {

    }

    public static String getFormattedDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        Integer month = cal.get(Calendar.MONTH);
        month = month + 1;

        String monthString;
        if (month < 10) {
            monthString = "0" + month;
        } else {
            monthString = month.toString();
        }

        int day = cal.get(Calendar.DAY_OF_MONTH);
        return year + "-" + monthString + "-" + day;
    }

    public static String getFormattedDate(Integer year, Integer month, Integer day) {
        String monthString;
        if (month < 10) {
            monthString = "0" + month;
        } else {
            monthString = month.toString();
        }

        return year + "-" + monthString + "-" + day;
    }
}
