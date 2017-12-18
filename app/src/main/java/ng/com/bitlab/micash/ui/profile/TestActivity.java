package ng.com.bitlab.micash.ui.profile;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rahimlis.badgedtablayout.BadgedTabLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.ViewPagerAdapter;
import ng.com.bitlab.micash.ui.addBanking.AddBankingActivity;

public class TestActivity extends AppCompatActivity {

    @BindView(R.id.test)
    AutoCompleteTextView test;
    @BindView(R.id.toolbar) Toolbar mToolbar;
    @BindView(R.id.text_view_title) TextView mTitle;
    boolean showNotification = true;


    private int[] activeIcons = {
            R.drawable.ic_wallet_white,
            R.drawable.ic_lists_white,
            R.drawable.ic_bells_white,
    };
    private int[] inactiveIcons = {
            R.drawable.ic_wallet,
            R.drawable.ic_lists,
            R.drawable.ic_bells,
    };
    private String[] tabTitles = {
            "Loans", "Ledger", "Notifications"
    };

    private String[] names = {
            "John", "Joel", "Joseph", "Hilda", "Mercy Johnson", "Abdul", "Barna", "Joy Abafi", "Joel Abafi", "Justina Abafi"
    };

    private String[] users = {};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTitle = (TextView) findViewById(R.id.text_view_title);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_TITLE);
        getSupportActionBar().setTitle("Hello");

        //initViewPager();
        initGuarantor();

    }

    public void initGuarantor(){

        final String currentUser = "paul tonga";//FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        final FirebaseDatabase db = FirebaseDatabase.getInstance();
        DatabaseReference userRef = db.getReference().child("users");

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //Completed

                ArrayList<String> suggestions = new ArrayList<>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    String suggestion = ds.child("fullName").getValue(String.class);
                    if(suggestion != null && !suggestion.equalsIgnoreCase(currentUser))
                        suggestions.add(suggestion);

                }
                Log.d("names", suggestions.toString());
                String[] s = suggestions.toArray(new String[suggestions.size()]);
                ArrayAdapter<String> adapter = new ArrayAdapter<>(TestActivity.this,R.layout.support_simple_spinner_dropdown_item, s);
                test.setAdapter(adapter);
                test.setThreshold(3);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }





}

