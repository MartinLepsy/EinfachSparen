package martinigt.einfachsparen.helper;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by martin on 16.04.16.
 */
public class Helper {

    public static    String getMonthForInt(int num) {
        String month = "---";
        DateFormatSymbols dfs = new DateFormatSymbols();
        String[] months = dfs.getMonths();
        if (num >= 0 && num <= 11 ) {
            month = months[num];
        }
        return month;
    }

    public static String getMonthForDate(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return Helper.getMonthForInt(cal.get(Calendar.MONTH));
    }

    public static long getDateDiff(Date date1, Date date2, TimeUnit timeUnit) {
        long diffInMillies = date2.getTime() - date1.getTime();
        return timeUnit.convert(diffInMillies, TimeUnit.MILLISECONDS);
    }
}
