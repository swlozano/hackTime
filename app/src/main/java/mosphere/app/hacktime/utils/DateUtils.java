package mosphere.app.hacktime.utils;

import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by alejandrolozanomedina on 24/10/16.
 */
public class DateUtils {

    public static Date resetTimeOnDate(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date.getTime());
        calendar.set(Calendar.HOUR,0);
        calendar.set(Calendar.MINUTE,0);
        calendar.set(Calendar.SECOND,0);
        calendar.set(Calendar.MILLISECOND,0);
        calendar.set(Calendar.AM_PM,calendar.AM);
        date.setTime(calendar.getTimeInMillis());
        return date;
    }

    public static long getDateDifference(Date dateStart, Date dateEnd) {
        long diffInMillies = dateEnd.getTime() - dateStart.getTime();
        return TimeUnit.MINUTES.convert(diffInMillies,TimeUnit.MILLISECONDS);
    }

    public static Date convertStringToDate(String strDate){
        long lgDate = Long.parseLong(strDate);
        return new Date(lgDate);
    }

    public static String formatDateDiff(long minutes){
        String txt="";

        int hr = (int) (minutes/60);
        int min = (int) (minutes%60);
        if(hr!=0||min!=0) {
            if(hr>0) {
                txt += hr > 0 ? hr : "0";
                txt += " hrs ";
            }
            if(min>0) {
                txt += min > 0 ? "" + min : "00";
                txt += " mins ";
            }
        }else{
            txt="0";
        }
        return txt;
    }

}
