package ng.com.bitlab.micash.common;


import android.app.Activity;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;

import butterknife.ButterKnife;
import butterknife.BindView;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.events.NotificationEvent;
import ng.com.bitlab.micash.models.Guarantor;
import ng.com.bitlab.micash.models.Notif;
import ng.com.bitlab.micash.models.Profile;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.ui.cards.CardsActivity;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.ui.guarantor.GuarantorActivity;
import ng.com.bitlab.micash.ui.message.ThreadActivity;
import ng.com.bitlab.micash.ui.profile.ProfileActivity;
import ng.com.bitlab.micash.ui.resume.ResumeActivity;
import ng.com.bitlab.micash.ui.transactions.TransactionsActivity;
import ng.com.bitlab.micash.utils.Constants;
import ng.com.bitlab.micash.utils.Utility;

import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MainActivity extends BaseView {

    private static final String TAG = "miCash";
    @BindView(R.id.viewPager) ViewPager mViewPager;
    @BindView(R.id.tabs) TabLayout mTabLayout;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.text_view_title) TextView mTitle;

    AccountHeader mHeader = null;
    Activity mActivity;
    Drawer mDrawer = null;

    public int badgeCount = 0;

    private int[] activeIcons = {
            R.drawable.ic_wallet_white,
            R.drawable.ic_lists_white,
            R.drawable.ic_bells_white};

    private int[] inactiveIcons = {
        R.drawable.ic_wallet,
        R.drawable.ic_lists,
        R.drawable.ic_bells};

    private String[] tabTitles = {
            "Loans", "Ledger", "Notifications"
    };

    private AppPreference mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser u  = FirebaseAuth.getInstance().getCurrentUser();
        if(u != null) {

            setContentView(R.layout.activity_main);
            ButterKnife.bind(this);
            mToolbar = (Toolbar) findViewById(R.id.toolbar);
            mTitle = (TextView) findViewById(R.id.text_view_title);
            setSupportActionBar(mToolbar);
            getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
            getSupportActionBar().setTitle("Hello");
            mActivity = this;
            savePreferences(Constants.DONE);

            EventBus.getDefault().register(this);

            mPref = MiCashApplication.getPreference();

            initializeOnlinePresence();

            lastLogin();
            d();

            updateProfile();

            if (mPref.getFirst() == null) {

            }
            firstLaunch();


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
                            new PrimaryDrawerItem().withName("Home").withIcon(FontAwesome.Icon.faw_home).withIdentifier(Constants.HOME),
                            new PrimaryDrawerItem().withName("Profile").withIcon(FontAwesome.Icon.faw_user).withIdentifier(Constants.PROFILE),
                            new PrimaryDrawerItem().withName("Banking Details").withIcon(FontAwesome.Icon.faw_credit_card).withIdentifier(Constants.BANKING),
                            new PrimaryDrawerItem().withName("Guarantor Requests").withIcon(FontAwesome.Icon.faw_shield).withIdentifier(Constants.GUARANTOR),
                            new PrimaryDrawerItem().withName("Transactions").withIcon(FontAwesome.Icon.faw_list).withIdentifier(Constants.TRANSACTIONS),
                            new PrimaryDrawerItem().withName("Terms and Conditions").withIcon(FontAwesome.Icon.faw_paragraph).withIdentifier(Constants.TERMS),
                            new PrimaryDrawerItem().withName("Logout").withIcon(FontAwesome.Icon.faw_sign_out).withIdentifier(Constants.LOGOUT)
                    )
                    .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                        @Override
                        public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                            if (drawerItem != null && drawerItem instanceof Nameable) {
                                String name = ((Nameable) drawerItem).getName().getText(MainActivity.this);
                                //mTitle.setText(name);
                                onTouchDrawer(drawerItem.getIdentifier());
                            }
                            return false;
                        }
                    })
                    .withSavedInstance(savedInstanceState)
                    .build();

            IntentFilter intentFilter = new IntentFilter("com.ng.bitlab.micash.CUSTOM_EVENT");
            LocalBroadcastManager.getInstance(this).registerReceiver(onMessage, intentFilter);


            initViewPager();
            Utility.updateInstanceID();
            getProfile();

            countUnreadNotification();
        }else{
            startLoginActivity();
        }
    }//e

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.actions_chat){
            startActivity(new Intent(this, ThreadActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private BroadcastReceiver onMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkNotifications();
            //getLastAdded();
            countUnreadNotification();
        }
    };

    private void getLastAdded(){

    }


    private void updateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final AppPreference pref = MiCashApplication.getPreference();

        /*pref.setName(user.getDisplayName());
        try {
            pref.setProfile(user.getPhotoUrl().toString());
        } catch (NullPointerException ex){
            //Log.d("miCash", ex.getMessage());
        }*/
        if(user != null) {
            User mUser = new User();
            mUser.setUuid(user.getUid());
            mUser.setFullName(user.getDisplayName());
            mUser.setEmail(user.getEmail());
            mUser.setPhoneNumber(user.getPhoneNumber());
            mUser.setProfileImage(user.getPhotoUrl() == null ? Constants.PROFILE_URL : user.getPhotoUrl().toString());
            mUser.setLastSeen(org.joda.time.DateTime.now().getMillis());
            mUser.setDateCreated(org.joda.time.DateTime.now().getMillis());
            //mUser.device = getDevice();

            Gson gson = new Gson();
            String json = gson.toJson(mUser);
            pref.setUser(json);
        }
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
                tab.setCustomView(null);
                tab.setCustomView(setUpTabView(position, true));
                mTitle.setText(tabTitles[position]);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                tab.setCustomView(null);
                tab.setCustomView(setUpTabView(position, false));
            }

        });




    }

    private void setTabIcons() {
        for(int i=0; i < 3; i++){
            TabLayout.Tab t = mTabLayout.getTabAt(i);
            if(t.getCustomView() != null)
                t.setCustomView(null);
            if(i == 0) {
                t.setCustomView(setUpTabView(i, true));
            }
            else {
                t.setCustomView(setUpTabView(i, false));
            }
        }
    }

    private View setUpTabView (int position, boolean isActive){
        View v = LayoutInflater.from(this).inflate(R.layout.badged_tab, null);
        ImageView icon = (ImageView) v.findViewById(R.id.icon);

        if(isActive){
            icon.setImageResource(activeIcons[position]);
        }
        else {
            icon.setImageResource(inactiveIcons[position]);
        }
        return v;
    }


    private void showBadge(int count){
        View v = LayoutInflater.from(this).inflate(R.layout.badged_tab, null);
        ImageView icon = (ImageView) v.findViewById(R.id.icon);
        TextView badge = (TextView) v.findViewById(R.id.badge);
        LinearLayout badgeContainer = (LinearLayout) v.findViewById(R.id.badgeCotainer);

        badge.setText(String.valueOf(count));
        badgeContainer.setVisibility(View.VISIBLE);
        icon.setImageResource(R.drawable.ic_bells);

        mTabLayout.getTabAt(2).setCustomView(null);
        mTabLayout.getTabAt(2).setCustomView(v);
    }

    private void onTouchDrawer(long identifier) {
        switch ((int) identifier){
            case Constants.HOME:
                //
                break;
            case Constants.PROFILE:
                startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                break;
            case Constants.BANKING:
                startActivity(new Intent(MainActivity.this, CardsActivity.class));
                break;
            case Constants.GUARANTOR:
                startActivity(new Intent(MainActivity.this, GuarantorActivity.class));
                break;
            case Constants.TRANSACTIONS:
                startActivity(new Intent(MainActivity.this, TransactionsActivity.class));
                break;
            case Constants.LOGOUT:
                FirebaseAuth.getInstance().signOut();
                startLoginActivity();
                break;
            case Constants.TERMS:
                startTermsAndConditions();

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


    private void lastLogin(){
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();

        ref.child("users").child(u.getUid()).child("lastSeen")
                .setValue(org.joda.time.DateTime.now().getMillis());


    }
    private void firstLaunch() {
        FirebaseUser u = FirebaseAuth.getInstance().getCurrentUser();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference ref = db.getReference();
        //mPref = MiCashApplication.getPreference();
        String token = FirebaseInstanceId.getInstance().getToken();

        ref.child("firstlaunch").child(u.getUid())
                .child("token")
                .setValue(token)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){

                            //save first launch to preference
                            mPref.setFirst(Constants.FIRST);

                        }
                    }
                });
    }

    private void checkNotifications(){
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference()
                .child("notifications")
                .child(uuid)
                .orderByKey().limitToLast(1)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot ds: dataSnapshot.getChildren()) {
                            Notif notif = ds.getValue(Notif.class);
                            //countUnreadNotification();
                            showNotification(notif.getTitle(), notif.getDescription());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

    }

    private void showNotification(String title, String body){

            Intent intent = new Intent(MiCashApplication.getContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 1,
                    intent, PendingIntent.FLAG_ONE_SHOT);

            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

            NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new
                    NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic)
                    .setContentTitle("miCash - " + title)
                    .setContentText(body)
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setContentIntent(pendingIntent);

            NotificationManager notificationManager = (NotificationManager)
                    getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());

    }

    private void countUnreadNotification(){
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference countRef = FirebaseDatabase.getInstance().getReference("notifications");
        countRef.keepSynced(true);

                countRef.child(uuid)
                .orderByChild("read")
                .equalTo(false)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                            long count = dataSnapshot.getChildrenCount();
                            if (count > 0) {
                                showBadge((int) count);
                            }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNotificationEvent(NotificationEvent event){
        countUnreadNotification();
    }

    private void d(){
        Guarantor g = new Guarantor();
        g.setInstanceID(FirebaseInstanceId.getInstance().getToken());
        g.setUuid(FirebaseAuth.getInstance().getCurrentUser().getUid());
        g.setEmail(getMD5(FirebaseAuth.getInstance().getCurrentUser().getEmail()));

        FirebaseDatabase.getInstance().getReference()
                .child("tokens")
                .child(g.getEmail())
                .setValue(g);
    }

    private void startTermsAndConditions(){
        final Dialog fullscreenDialog = new Dialog(this, R.style.DialogFullscreen);
        fullscreenDialog.setContentView(R.layout.dialog_fullscreen);
        ImageView img_dialog_fullscreen_close = (ImageView) fullscreenDialog.findViewById(R.id.img_dialog_fullscreen_close);
        TextView tv = (TextView) fullscreenDialog.findViewById(R.id.tv_terms_and_conditions);
        tv.setMovementMethod(new ScrollingMovementMethod());
        img_dialog_fullscreen_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullscreenDialog.dismiss();
            }
        });
        fullscreenDialog.show();
    }


    private static String getMD5(String email) {

        StringBuffer _sb = new StringBuffer();

        try {

            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(email.getBytes());

            byte byteData[] = md.digest();

            for(int i = 0; i<byteData.length; i++){
                _sb.append(Integer.toString((byteData[i] & 0xff)+0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return _sb.toString();
    }

    public void getProfile() {
        String userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseDatabase.getInstance().getReference()
                .child("profile")
                .child(userID)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot != null){
                            Profile profile = dataSnapshot.getValue(Profile.class);
                            if (profile != null){
                                mPref.setEmploymentSaved(Constants.DONE);
                            }
                        }else {

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
