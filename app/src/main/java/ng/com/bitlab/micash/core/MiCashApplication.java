package ng.com.bitlab.micash.core;

import android.app.Application;
import android.content.Context;


import net.orange_box.storebox.StoreBox;


import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.common.FontsOverride;
import ng.com.bitlab.micash.models.DaoMaster;
import ng.com.bitlab.micash.models.DaoSession;


/**
 * Created by Paul on 12/06/2017.
 */

public class MiCashApplication extends Application {


    private static Context context;
    private static AppPreference mPreferences;
    private DaoSession mDaoSession;



    @Override
    public void onCreate() {
        super.onCreate();
        FontsOverride.setDefaultFont(this, "SERIF", "hnlight.ttf");
        FontsOverride.setDefaultFont(this, "SERIF_BOLD", "hnbold.ttf");
        FontsOverride.setDefaultFont(this, "SERIF_THIN", "hnthin.ttf");
        FontsOverride.setDefaultFont(this, "SERIF_MEDIUM", "hnmedium.ttf");

        context = getApplicationContext();

        mPreferences = StoreBox.create(context, AppPreference.class);
    }

    public static Context getContext(){
        return context;
    }


    public static AppPreference getPreference() { return mPreferences; }

}
