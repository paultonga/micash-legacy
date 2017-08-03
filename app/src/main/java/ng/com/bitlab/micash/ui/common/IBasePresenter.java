package ng.com.bitlab.micash.ui.common;

/**
 * Created by Paul on 22/07/2017.
 */

public interface IBasePresenter<ViewT> {

    void onViewActive(ViewT view);

    void onViewInactive();
}
