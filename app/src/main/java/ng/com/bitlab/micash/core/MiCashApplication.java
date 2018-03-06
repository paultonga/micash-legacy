package ng.com.bitlab.micash.core;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;


import com.google.firebase.database.FirebaseDatabase;
import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

import net.orange_box.storebox.StoreBox;


import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.common.FontsOverride;


/**
 * Created by Paul on 12/06/2017.
 */

public class MiCashApplication extends Application {


    private static Context context;
    private static AppPreference mPreferences;
    public static Bus bus;




    @Override
    public void onCreate() {
        super.onCreate();
        bus = new Bus(ThreadEnforcer.ANY);

        FontsOverride.setDefaultFont(this, "SERIF", "hnlight.ttf");
        FontsOverride.setDefaultFont(this, "SERIF_BOLD", "hnbold.ttf");
        FontsOverride.setDefaultFont(this, "SERIF_THIN", "hnthin.ttf");
        FontsOverride.setDefaultFont(this, "SERIF_MEDIUM", "hnmedium.ttf");

        context = getApplicationContext();

        mPreferences = StoreBox.create(context, AppPreference.class);

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    public static Context getContext(){
        return context;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public static AppPreference getPreference() { return mPreferences; }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
