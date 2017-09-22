package ng.com.bitlab.micash.common;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;

import butterknife.ButterKnife;
import butterknife.BindView;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.ui.cards.CardsActivity;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.ui.guarantor.GuarantorActivity;
import ng.com.bitlab.micash.ui.resume.ResumeActivity;
import ng.com.bitlab.micash.ui.settings.SettingsActivity;
import ng.com.bitlab.micash.ui.transactions.TransactionsActivity;
import ng.com.bitlab.micash.utils.Constants;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;

public class MainActivity extends BaseView {

    private static final String TAG = "miCash";
    @BindView(R.id.viewPager) ViewPager mViewPager;
    @BindView(R.id.tabs) TabLayout mTabLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.text_view_title) TextView mTitle;

    AccountHeader mHeader = null;
    Activity mActivity;
    Drawer mDrawer = null;

    private int[] activeIcons = {
            R.drawable.ic_loan_white,
            R.drawable.ic_ledger_white,
            R.drawable.ic_notification_white,
            R.drawable.ic_user_white
    };
    private int[] inactiveIcons = {
            R.drawable.ic_loan_dark,
            R.drawable.ic_ledger_dark,
            R.drawable.ic_notification_dark,
            R.drawable.ic_user_dark
    };
    private String[] tabTitles = {
            "Loans", "Ledger", "Notifications", "Profile"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.text_view_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setTitle("Hello");
        mActivity = this;
        savePreferences(Constants.DONE);

        initializeOnlinePresence();

        lastLogin();

        //dummy();


        mHeader = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSavedInstance(savedInstanceState)
                .build();

        mDrawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(mHeader)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_shopping_cart).withIdentifier(Constants.HOME),
                        new PrimaryDrawerItem().withName("Transactions").withIcon(FontAwesome.Icon.faw_bar_chart).withIdentifier(Constants.TRANSACTIONS),
                        new PrimaryDrawerItem().withName("Cards").withIcon(FontAwesome.Icon.faw_cog).withIdentifier(Constants.CARDS),
                        new PrimaryDrawerItem().withName("Guarantor Requests").withIcon(FontAwesome.Icon.faw_cog).withIdentifier(Constants.GUARANTOR),
                        new PrimaryDrawerItem().withName("Settings").withIcon(FontAwesome.Icon.faw_credit_card).withIdentifier(Constants.SETTINGS),
                        new PrimaryDrawerItem().withName("Logout").withIcon(FontAwesome.Icon.faw_credit_card).withIdentifier(Constants.LOGOUT)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem != null && drawerItem instanceof Nameable){
                            String name = ((Nameable)drawerItem).getName().getText(MainActivity.this);
                            //mTitle.setText(name);
                            onTouchDrawer(drawerItem.getIdentifier());
                        }
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();


        initViewPager();

    }

    private void initializeOnlinePresence(){

        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference databaseReference = db.getReference();

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


        final DatabaseReference onlineRef =  databaseReference.child(".info/connected");
        final DatabaseReference currentUserRef = databaseReference.child("/presence/" + userId);
        onlineRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {
                Log.d(TAG, "DataSnapshot:" + dataSnapshot);
                if (dataSnapshot.getValue(Boolean.class)){
                    currentUserRef.onDisconnect().removeValue();
                    currentUserRef.setValue(true);
                }
            }

            @Override
            public void onCancelled(final DatabaseError databaseError) {
                Log.d(TAG, "DatabaseError:" + databaseError);
            }
        });

    }

    private void initViewPager() {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        setTabIcons();

        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager){
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tab.setIcon(activeIcons[position]);
                mTitle.setText(tabTitles[position]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tab.setIcon(inactiveIcons[position]);
            }

        });




    }

    private void setTabIcons() {
        mTabLayout.getTabAt(0).setIcon(R.drawable.ic_loan_white);
        mTabLayout.getTabAt(1).setIcon(R.drawable.ic_ledger_dark);
        mTabLayout.getTabAt(2).setIcon(R.drawable.ic_notification_dark);
        mTabLayout.getTabAt(3).setIcon(R.drawable.ic_user_dark);
    }

    private void onTouchDrawer(long identifier) {
        switch ((int) identifier){
            case Constants.HOME:
                //
                break;
            case Constants.TRANSACTIONS:
                startActivity(new Intent(MainActivity.this, TransactionsActivity.class));
                break;
            case Constants.CARDS:
                startActivity(new Intent(MainActivity.this, CardsActivity.class));
                break;
            case Constants.GUARANTOR:
                startActivity(new Intent(MainActivity.this, GuarantorActivity.class));
                break;
            case Constants.SETTINGS:
                //Go to checkout page
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case Constants.LOGOUT:
                FirebaseAuth.getInstance().signOut();
                startLoginActivity();
                //Toast.makeText(this,"Log out has not been implemented yet", Toast.LENGTH_SHORT).show();
                break;

        }

    }

    private void startLoginActivity() {
        Intent intent = new Intent(this, ResumeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void savePreferences(String s) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(Constants.APP_STATE, s);
        editor.apply();
    }

    private void dummy(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String phone = sp.getString(Constants.PHONE, null);
        String code = sp.getString(Constants.CODE, null);

        showToast(phone + " "+ code);

    }

    private void lastLogin(){
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();

        ref.child("users").child(u.getUid()).child("lastSeen")
                .setValue(org.joda.time.DateTime.now().getMillis());


    }

}
