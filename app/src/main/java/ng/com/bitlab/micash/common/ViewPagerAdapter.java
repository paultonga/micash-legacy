package ng.com.bitlab.micash.common;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import ng.com.bitlab.micash.R;
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
            default:
                selectedFragment = new LoansListFragment();
        }
        return selectedFragment;
    }

    @Override
    public int getCount() {
        return 3;
    }



}
