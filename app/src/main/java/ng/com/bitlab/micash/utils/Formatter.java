package ng.com.bitlab.micash.utils;

import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.PeriodFormat;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Paul on 12/06/2017.
 */

public class Formatter {

    private static String naira = "\u20a6";
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

    public static String getCurrencyText(String input){

        String output = input.replaceAll(",", "");
        long number = Long.parseLong(output);

        DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = numberFormat.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        numberFormat.setDecimalFormatSymbols(symbols);
        numberFormat.setMinimumFractionDigits(0);

        return naira+numberFormat.format(number);
    }

    public static String getBoldText(String text){
        SpannableString ss = new SpannableString(text);
        ss.setSpan(new StyleSpan(Typeface.BOLD), 0, text.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return ss.toString();
    }

    public static String getBoldCurrencyText(String text){
        return getBoldText(getCurrencyText(text));
    }
}
