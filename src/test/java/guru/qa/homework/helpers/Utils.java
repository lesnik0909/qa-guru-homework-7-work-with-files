package guru.qa.homework.helpers;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Utils {

  public static Date getTime(String dateString) {
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy",
        Locale.ENGLISH);
    try {
      calendar.setTime(simpleDateFormat.parse(dateString));
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return calendar.getTime();
  }

}
