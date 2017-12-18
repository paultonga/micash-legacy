package ng.com.bitlab.micash.ui.cards;

import java.util.List;

import ng.com.bitlab.micash.models.AccountRecord;
import ng.com.bitlab.micash.models.BankRecord;

/**
 * Created by paul on 12/3/17.
 */

public interface CardsContract {

    interface View {

        void showEmptyLayout();

        void showCards(List<BankRecord> records);

    }

    interface Presenter {
        void loadData();
    }
}
