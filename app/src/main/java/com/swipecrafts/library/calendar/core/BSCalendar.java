package com.swipecrafts.library.calendar.core;

import com.swipecrafts.school.utils.Utils;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by Madhusudan Sapkota on 3/8/2018.
 */

public class BSCalendar extends BaseCalendar {

    private static final String[] days = new String[]{"०", "१", "२", "३", "४", "५", "६", "७", "८", "९"};
    private static final String[] days_of_weeks = new String[]{"आइतवार", "सोमवार", "मंगलवार", "बुधवार", "बिहिवार", "शुक्रवार", "शनिवार"};
    private static final String[] days_of_weeks_short = new String[]{"आइ", "सोम", "मंग", "बुध", "बिहि", "शुक्र", "शनि"};
    private static final String[] months = new String[]{"वैशाख", "जेठ", "आषाढ", "साउन", "भाद्र", "आश्विन", "कार्तिक", "मंसीर", "पौष", "माघ", "फाल्गुन", "चैत्र"};
    private static final String[] months_short = new String[]{"वैशाख", "जेठ", "आषाढ", "साउन", "भाद्र", "आश्विन", "कार्तिक", "मंसीर", "पौष", "माघ", "फाल्गुन", "चैत्र"};
    private static TimeZone tz = TimeZone.getTimeZone("Asia/Katmandu");
    private final String[] days_of_weeks_roman = new String[]{"Aaitabar", "Sombar", "Mangalbar", "Budhabar", "Bihibar", "Sukrabar", "Sanibar"};
    private final String[] months_roman = new String[]{"Bhaishak ", "Jestha", "Asadh", "Shrawan", "Bhadra", "Ashwin", "Kartik", "Mangsir", "Paush", "Magh", "Falgun", "Chaitra"};
    private final int[][] yearMonthSpanLookupTable = new int[][]{new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 32, 31, 30, 29, 30, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31}, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31}, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{30, 32, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31}, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 32, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{30, 32, 31, 32, 31, 31, 29, 30, 29, 30, 29, 31}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 32, 31, 30, 29, 30, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 29, 31}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{31, 31, 31, 32, 31, 31, 29, 30, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 31}, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 29, 31}, new int[]{31, 31, 31, 32, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 29, 30, 30}, new int[]{31, 31, 32, 32, 31, 30, 30, 30, 29, 30, 30, 30}, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30}, new int[]{31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30}, new int[]{31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30}, new int[]{31, 32, 31, 32, 30, 31, 30, 30, 29, 30, 30, 30}, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 30, 29, 30, 30, 30}, new int[]{30, 31, 32, 32, 30, 31, 30, 30, 29, 30, 30, 30}, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30}, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 30, 29, 30, 30, 30}, new int[]{30, 31, 32, 32, 31, 30, 30, 30, 29, 30, 30, 30}, new int[]{30, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30}, new int[]{31, 31, 32, 31, 31, 30, 30, 30, 29, 30, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 30, 30, 30, 30}, new int[]{30, 31, 32, 32, 31, 30, 30, 29, 30, 29, 30, 30}, new int[]{31, 32, 31, 32, 31, 30, 30, 30, 29, 30, 30, 30}, new int[]{31, 31, 32, 31, 31, 31, 29, 30, 29, 30, 29, 31}, new int[]{31, 31, 32, 31, 31, 31, 30, 29, 29, 30, 30, 31}};

    public BSCalendar(int year, int month, int day, int hour, int minute, int second) {
        super(year, month, day, hour, minute, second);
    }

    public BSCalendar() {
        super(2059, 1, 1, 0, 0, 0);
    }

    public BSCalendar(Date date) {
        super(from(date, BS));
    }

    public BSCalendar(int year, int month, int day) {
        super(year, month, day);
    }

    public static BSCalendar today() {
        synchronized (BSCalendar.class) {
            GregorianCalendar gregorianCalendar = new GregorianCalendar(tz);
            BSCalendar convert = new BSCalendar();
            convert.setTime(gregorianCalendar.getTime());
            return convert;
        }
    }

    public static String toNepali(int value) {
        String nepaliWord = "";

        if (value < 0) {
            value = Math.abs(value);
            nepaliWord = "-";
        }

        if (value < 10) return nepaliWord + BSCalendar.days[value];
        else {
            int counter = 0;
            String text = String.valueOf(value);

            StringBuilder nepaliWordBuilder = new StringBuilder(nepaliWord);
            while (counter < text.length()) {
                nepaliWordBuilder.append(days[Integer.parseInt("" + text.charAt(counter))]);
                counter++;
            }
            nepaliWord = nepaliWordBuilder.toString();
            return nepaliWord;
        }
    }

    private static int getYearIndex(int year) {
        int index = (year - 1999) % 100;
        return index < 0 ? index * -1 : index;
    }

    public static void main(String[] args) {
        Date date = Calendar.getInstance().getTime();
//        System.out.println(BSCalendar.from(date, BS));
        System.out.println(BSCalendar.today());
    }

    @Override
    public String[] days() {
        return days;
    }

    @Override
    protected String[] daysOfWeeks() {
        return days_of_weeks;
    }

    @Override
    public String[] daysOfWeeksShort() {
        return days_of_weeks_short;
    }

    @Override
    protected String[] months() {
        return months;
    }

    @Override
    protected String[] monthsShort() {
        return months_short;
    }

    @Override
    public BaseCalendar getReferenceDate() {
        return new BSCalendar();
    }

    @Override
    public int getMaximumDaysInMonth() {
        return yearMonthSpanLookupTable[getYearIndex(this.year)][this.month - 1];
    }

    @Override
    public int getMaximumDaysInMonth(int year, int month) {
        return yearMonthSpanLookupTable[getYearIndex(year)][month - 1];
    }

    @Override
    public int getMaximumDaysInYear() {
        return Utils.addAll(yearMonthSpanLookupTable[getYearIndex(this.year)]);
    }

    @Override
    public int getMaximumDaysInYear(int year) {
         return Utils.addAll(yearMonthSpanLookupTable[getYearIndex(year)]);
    }

    @Override
    public int getFirstDayOfWeek() {
        return 0;
    }

    @Override
    public boolean isThisMonth() {
        BaseCalendar today = BSCalendar.today();
        return month == today.getMonth();
    }

    @Override
    public int getType() {
        return BS;
    }


}
