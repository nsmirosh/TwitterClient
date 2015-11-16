package miroshnychenko.mykola.twitterclient.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class TwitterDateHelper {


    public static long parseDate(String date)  {

        SimpleDateFormat inFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss ZZZZZ yyyy", Locale.getDefault());
        inFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

        Date date1 = null;
        try {
            date1 = inFormat.parse(date);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        inFormat.setTimeZone(TimeZone.getDefault());
        inFormat.format(date1);
        return date1.getTime();
    }

}
