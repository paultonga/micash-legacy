package ng.com.bitlab.micash.ui.common;

import android.view.View;

/**
 * Created by Paul on 22/07/2017.
 */

public class BasePresenter<ViewT> implements IBasePresenter<ViewT> {

    protected ViewT view;

    @Override
    public void onViewActive(ViewT view) {
        this.view = view;
    }

    @Override
    public void onViewInactive() {
        this.view = null;
    }
}
