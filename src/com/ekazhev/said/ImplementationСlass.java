package com.ekazhev.said;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.*;


import java.lang.StringBuilder;
import java.util.stream.Stream;

public class ImplementationСlass implements DatesToCronConverter{

    public boolean isValid(LocalDateTime dateTime){
        String pattern = "yyyy-MM-ddTHH:mm:ss";

        String strDataTime = ""+ dateTime;
        if (strDataTime.length()==pattern.length()) {
            return Character.toString(strDataTime.charAt(4)).equals(Character.toString(pattern.charAt(4))) && Character.toString(strDataTime.charAt(7)).equals(Character.toString(pattern.charAt(7)))
                    && Character.toString(strDataTime.charAt(13)).equals(Character.toString(pattern.charAt(13)))
                    && Character.toString(strDataTime.charAt(16)).equals(Character.toString(pattern.charAt(16)));
        }

        return Character.toString(strDataTime.charAt(4)).equals(Character.toString(pattern.charAt(4))) && Character.toString(strDataTime.charAt(7)).equals(Character.toString(pattern.charAt(7)))
                && Character.toString(strDataTime.charAt(13)).equals(Character.toString(pattern.charAt(13)));

    }

    public String convert(List<LocalDateTime> listDate){

        StringBuilder cron = new StringBuilder();
        int count = 0;
        int minSecond  = listDate.get(0).getSecond();
        int maxSecond  = listDate.get(0).getSecond();
        int minuteMIN = listDate.get(0).getMinute();
        int minuteMAX = listDate.get(0).getMinute();
        int hourMIN = listDate.get(0).getHour();
        int hourMAX = listDate.get(0).getHour();

        int dayMIN =  listDate.get(0).getDayOfMonth();
        int dayMAX = listDate.get(0).getDayOfMonth();
        int monthMIN= listDate.get(0).getMonthValue();
        int monthMAX = listDate.get(0).getMonthValue();
        int weekMinVal = listDate.get(0).getDayOfWeek().getValue();
        int weekMaxVal = listDate.get(0).getDayOfWeek().getValue();
        DayOfWeek weekMin = null;
        DayOfWeek weekMax = null;

        for (int i =0; i< listDate.size(); i++) {
            if (isValid(listDate.get(i))) {

                if (i < listDate.size() - 3 && (Math.abs(listDate.get(i).getSecond() - listDate.get(i + 1).getSecond()) != Math.abs(listDate.get(i + 2).getSecond() - listDate.get(i + 3).getSecond())
                        || Math.abs(listDate.get(i).getMinute() - listDate.get(i + 1).getMinute()) != Math.abs(listDate.get(i + 2).getMinute() - listDate.get(i + 3).getMinute())
                        || Math.abs(listDate.get(i).getHour() - listDate.get(i + 1).getHour()) != Math.abs(listDate.get(i + 2).getHour() - listDate.get(i + 3).getHour())
                        || Math.abs(listDate.get(i).getDayOfMonth() - listDate.get(i + 1).getDayOfMonth()) != Math.abs(listDate.get(i + 2).getDayOfMonth() - listDate.get(i + 3).getDayOfMonth())
                        || Math.abs(listDate.get(i).getMonthValue() - listDate.get(i + 1).getMonthValue()) != Math.abs(listDate.get(i + 2).getMonthValue() - listDate.get(i + 3).getMonthValue())
                        || Math.abs(listDate.get(i).getDayOfWeek().getValue() - listDate.get(i + 1).getDayOfWeek().getValue()) != Math.abs(listDate.get(i + 2).getDayOfWeek().getValue() - listDate.get(i + 3).getDayOfWeek().getValue())
                )) { count++; }
                if (count >= listDate.size()/2){ throw  new DatesToCronConvertException(); }

                if (listDate.get(i).getMinute() < minuteMIN) minuteMIN = listDate.get(i).getMinute();
                if (listDate.get(i).getMinute() > minuteMAX) minuteMAX = listDate.get(i).getMinute();

                if (listDate.get(i).getSecond() < minSecond) minSecond = listDate.get(i).getSecond();
                if (listDate.get(i).getSecond() > maxSecond) maxSecond = listDate.get(i).getSecond();

                if (listDate.get(i).getHour() < hourMIN) hourMIN = listDate.get(i).getHour();
                if (listDate.get(i).getHour() > hourMAX) hourMAX = listDate.get(i).getHour();

                if (listDate.get(i).getDayOfMonth() < dayMIN) dayMIN = listDate.get(i).getDayOfMonth();
                if (listDate.get(i).getDayOfMonth() > dayMAX) dayMAX = listDate.get(i).getDayOfMonth();

                if (listDate.get(i).getMonthValue() < monthMIN) monthMIN = listDate.get(i).getMonthValue();
                if (listDate.get(i).getMonthValue() > monthMAX) monthMAX = listDate.get(i).getMonthValue();

                if (listDate.get(i).getDayOfWeek().getValue() < weekMinVal)
                    weekMinVal = listDate.get(i).getDayOfWeek().getValue();
                weekMax = listDate.get(i).getDayOfWeek();
                if (listDate.get(i).getDayOfWeek().getValue() > weekMaxVal)
                    weekMaxVal = listDate.get(i).getDayOfWeek().getValue();
                weekMin = listDate.get(i).getDayOfWeek();
            } else {throw new DatesToCronConvertException();}
        }


        if ( minSecond  == maxSecond && (minSecond != 0 && maxSecond != 0)) { cron.append("* "); }
        else if ( minSecond  == maxSecond && minSecond == 0) { cron.append("0 "); }
        else if (listDate.get(0).getSecond() == listDate.get(2).getSecond()) { cron.append("*/").append(Math.abs(listDate.get(0).getSecond()-listDate.get(1).getSecond())).append(" "); }
        else if (listDate.get(0).getSecond() - listDate.get(1).getSecond() ==1) {cron.append("["+ minSecond+"-"+maxSecond+"] ");}
        else count++;

        if ( minuteMIN  == minuteMAX && (minuteMIN != 0 && minuteMAX !=0)) { cron.append("* "); }
        else if ( minuteMIN  == minuteMAX && minuteMIN == 0) { cron.append("0 "); }
        else if (listDate.get(0).getMinute() == listDate.get(2).getMinute()) { cron.append("*/").append(Math.abs(listDate.get(0).getMinute()-listDate.get(1).getMinute())).append(" "); }
        else if (listDate.get(0).getMinute() - listDate.get(2).getMinute() == 1) {cron.append("["+ minuteMIN+"-"+minuteMAX+"] ");}

        if ( hourMIN  == hourMAX && (hourMIN != 0 && hourMAX != 0) ) { cron.append("* "); }
        else if ( hourMIN  == hourMAX && hourMIN == 0 ) { cron.append("0 "); }
        else if ( Math.abs(listDate.get(0).getHour()-listDate.get(1).getHour()) == 1 ) { cron.append("*/").append(Math.abs(listDate.get(0).getHour()-listDate.get(0).getHour())).append(" "); }
        else {cron.append("["+ hourMIN+"-"+hourMAX+"] ");}

        if ( dayMIN  == dayMAX && (dayMIN != 0 && dayMAX != 0) ) { cron.append("* "); }
        else if (Math.abs(listDate.get(0).getDayOfMonth()-listDate.get(1).getDayOfMonth())==1) { cron.append("*/").append(Math.abs(listDate.get(0).getDayOfMonth()-listDate.get(0).getDayOfMonth())).append(" "); }
        else {cron.append("["+ dayMIN+"-"+dayMAX+"] ");}

        if ( monthMIN  == monthMAX && (monthMIN != 0 && monthMAX != 0)) { cron.append("* "); }
        else if (Math.abs(listDate.get(0).getMonthValue()-listDate.get(1).getMonthValue())==1) { cron.append("*/").append(Math.abs(listDate.get(0).getMonthValue()-listDate.get(0).getMonthValue())).append(" "); }
        else {cron.append("["+ monthMIN+"-"+monthMAX+"] ");}

        if ( weekMinVal  == weekMaxVal && (weekMinVal !=0 && weekMaxVal !=0)) { cron.append("* "); }
        else if ( weekMinVal  == weekMaxVal && weekMinVal == 0) { cron.append("0 "); }
        else if (Math.abs(listDate.get(0).getDayOfWeek().getValue()-listDate.get(1).getDayOfWeek().getValue())==1) { cron.append("*/").append(Math.abs(listDate.get(0).getDayOfWeek().getValue()-listDate.get(0).getDayOfWeek().getValue())).append(" "); }
        else {cron.append("["+ weekMin.toString()+"-"+weekMax.toString()+"] ");}

        return cron.toString();
    }


    public String getImplementationInfo(){
        ImplementationСlass i = new ImplementationСlass();
        return "Экажев Саид Магомед Мусаевич" + ", "+ this.getClass().getPackage().getName() +", "+ this.getClass().getSimpleName()+", https://github.com/SadBoyCarti" +
                "gn/tree/master/src/com/ekazhev/said";
    }
    public static void main(String[] args){

    }
}