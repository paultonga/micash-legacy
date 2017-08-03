package ng.com.bitlab.micash.core;

import android.app.Application;
import android.content.Context;

import ng.com.bitlab.micash.common.FontsOverride;

/**
 * Created by Paul on 12/06/2017.
 */

public class MiCashApplication extends Application {


    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "SERIF", "hnlight.ttf");
        FontsOverride.setDefaultFont(this, "SERIF_BOLD", "hnbold.ttf");
        FontsOverride.setDefaultFont(this, "SERIF_THIN", "hnthin.ttf");
        FontsOverride.setDefaultFont(this, "SERIF_MEDIUM", "hnmedium.ttf");

        context = getApplicationContext();
    }

    public static Context getContext(){
        return context;
    }
}
