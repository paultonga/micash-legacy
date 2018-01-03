package ng.com.bitlab.micash.ui.cards;

import java.util.List;

import ng.com.bitlab.micash.models.AccountRecord;
import ng.com.bitlab.micash.models.BankRecord;

/**
 * Created by paul on 12/3/17.
 */

public class CardsPresenter implements CardsContract.Presenter {

    CardsContract.View view;

    public CardsPresenter(CardsContract.View view) {
        this.view = view;
    }

    @Override
    public void loadData() {
        List<BankRecord> accountRecord = BankRecord.listAll(BankRecord.class);
        if(accountRecord == null || accountRecord.isEmpty()){
            view.showEmptyLayout();
        } else {
            view.showCards(accountRecord);
        }
    }
}
