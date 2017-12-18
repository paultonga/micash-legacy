package ng.com.bitlab.micash.utils;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Created by Paul on 12/06/2017.
 */

public class Formatter {

    private static final char[] magnitudes = {'k', 'M', 'G', 'T', 'P', 'E'}; // enough for long

    public static final String numberFormat(long number) {
        String ret;
        if (number >= 0) {
            ret = "";
        } else if (number <= -9200000000000000000L) {
            return "-9.2E";
        } else {
            ret = "-";
            number = -number;
        }
        if (number < 1000)
            return ret + number;
        for (int i = 0; ; i++) {
            if (number < 10000 && number % 1000 >= 100)
                return ret + (number / 1000) + '.' + ((number % 1000) / 100) + magnitudes[i];
            number /= 1000;
            if (number < 1000)
                return ret + number + magnitudes[i];
        }
    }


    public static String TimeFormatter(long time){

        DateTime past = new DateTime(time);
        DateTime now = new DateTime();
        Period period = new Period(past, now);

        PeriodFormatter formatter;

        if(period.getYears() != 0){
            formatter = new PeriodFormatterBuilder().appendYears().appendSuffix(" years ago").printZeroNever().toFormatter();
        }else if(period.getMonths() !=0){
            formatter = new PeriodFormatterBuilder().appendMonths().appendSuffix(" months").printZeroNever().toFormatter();
        }else if(period.getWeeks() !=0){
            formatter = new PeriodFormatterBuilder().appendWeeks().appendSuffix(" weeks ago").printZeroNever().toFormatter();
        }else if(period.getDays() !=0){
            formatter = new PeriodFormatterBuilder().appendDays().appendSuffix(" days ago").printZeroNever().toFormatter();
        }else if(period.getHours() !=0){
            formatter = new PeriodFormatterBuilder().appendHours().appendSuffix(" hours ago").printZeroNever().toFormatter();
        }else if(period.getMinutes() !=0){
            formatter = new PeriodFormatterBuilder().appendMinutes().appendSuffix(" minutes ago").printZeroNever().toFormatter();
        }else{
            formatter = new PeriodFormatterBuilder().appendSeconds().appendSuffix(" seconds ago").printZeroNever().toFormatter();
        }

        return formatter.print(period);


    }
}
