package ng.com.bitlab.micash.ui.common;

import android.content.Context;

/**
 * Created by Paul on 22/07/2017.
 */

public interface IBaseVew {

    void showDialog(String message);

    void hideDialog();

    void showToast(String message);

    void showSnackBar(String message);

    Context getActivityContext();

}
