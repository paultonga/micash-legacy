package ng.com.bitlab.micash.listeners;

import ng.com.bitlab.micash.models.Ledger;

/**
 * Created by Paul Tonga on 12/02/2018.
 */

public interface LedgerTouchedListener {
    void onLedgerTouched(Ledger ledger, int position);
}
