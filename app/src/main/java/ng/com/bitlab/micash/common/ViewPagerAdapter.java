package ng.com.bitlab.micash.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import ng.com.bitlab.micash.ui.ledger.LedgerListFragment;
import ng.com.bitlab.micash.ui.loans.LoansListFragment;
import ng.com.bitlab.micash.ui.notifications.NotificationsListFragment;
import ng.com.bitlab.micash.ui.profile.ProfileFragment;

/**
 * Created by Paul on 13/06/2017.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    @Override
    public CharSequence getPageTitle(int position) {
        String title = "";

        /*switch(position){
            case 0:
                title = "Loans";
                break;
            case 1:
                title = "Ledger";
                break;
            case 2:
                title = "Notifications";
                break;
            default:
                title = "";
                break;

        }*/
        return title;
    }

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment selectedFragment;

        switch (position){
            case 0:
                selectedFragment = new LoansListFragment();
                break;
            case 1:
                selectedFragment = new LedgerListFragment();
                break;
            case 2:
                selectedFragment = new NotificationsListFragment();
                break;
            case 3:
                selectedFragment = new ProfileFragment();
                break;
            default:
                selectedFragment = new LoansListFragment();
        }
        return selectedFragment;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
