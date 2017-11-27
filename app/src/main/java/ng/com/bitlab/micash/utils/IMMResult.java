package ng.com.bitlab.micash.utils;


import android.os.Bundle;
import android.util.Log;

/**
 * Created by paul on 11/27/17.
 */

public class IMMResult extends android.os.ResultReceiver {

    public int result = -1;

    public IMMResult() {
        super(null);
    }

    @Override
    public void onReceiveResult(int r, Bundle data) {
        result = r;
    }

    // poll result value for up to 500 milliseconds
    public int getResult() {
        try {
            int sleep = 0;
            while (result == -1 && sleep < 500) {
                Thread.sleep(100);
                sleep += 100;
            }
        } catch (InterruptedException e) {
            Log.e("IMMResult", e.getMessage());
        }
        return result;
    }
}
