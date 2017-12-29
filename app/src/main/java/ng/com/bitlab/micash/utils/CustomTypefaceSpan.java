package ng.com.bitlab.micash.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.style.MetricAffectingSpan;

/**
 * Created by Paul Tonga on 29/12/2017.
 */

public class CustomTypefaceSpan extends MetricAffectingSpan {

    private final Typeface typeface;

    public CustomTypefaceSpan(Typeface typeface) {
        this.typeface = typeface;
    }

    @Override
    public void updateMeasureState(TextPaint textPaint) {
        applyCustomTypeface(textPaint, typeface);
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        applyCustomTypeface(textPaint, typeface);
    }

    private static void applyCustomTypeface(Paint paint, Typeface typeface){
        paint.setTypeface(typeface);
    }
}
