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

        Integer day = cal.get(Calendar.DAY_OF_MONTH);
        String dayString;
        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = day.toString();
        }

        return year + "-" + monthString + "-" + dayString;
    }

    public static String getFormattedDate(Integer year, Integer month, Integer day) {
        String monthString;
        if (month < 10) {
            monthString = "0" + month;
        } else {
            monthString = month.toString();
        }

        String dayString;
        if (day < 10) {
            dayString = "0" + day;
        } else {
            dayString = day.toString();
        }

        return year + "-" + monthString + "-" + dayString;
    }

    public static String getMonthFormat(int month) {
        if(month == 1) {
            return "JAN";
        }
        if(month == 2) {
            return "FEV";
        }
        if(month == 3) {
            return "MAR";
        }
        if(month == 4) {
            return "ABR";
        }
        if(month == 5) {
            return "MAI";
        }
        if(month == 6) {
            return "JUN";
        }
        if(month == 7) {
            return "JUL";
        }
        if(month == 8) {
            return "AGO";
        }
        if(month == 9) {
            return "SET";
        }
        if(month == 10) {
            return "OUT";
        }
        if(month == 11) {
            return "NOV";
        }
        if(month == 12) {
            return "DEZ";
        }
        return "JAN";
    }

    public static String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return DBUtils.makeDateString(day, month, year);
    }

    public static String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }
}
