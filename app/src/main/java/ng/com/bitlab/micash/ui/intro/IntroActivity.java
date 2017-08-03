package ng.com.bitlab.micash.ui.intro;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;



import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.ui.register.RegisterActivity;
import ng.com.bitlab.micash.utils.Constants;

public class IntroActivity extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private Button slideButton;

    ImageView zero, one, two;
    ImageView[] indicators;



    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    public int page;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        page = 0;

        zero = (ImageView) findViewById(R.id.intro_indicator_0);
        one = (ImageView) findViewById(R.id.intro_indicator_1);
        two = (ImageView) findViewById(R.id.intro_indicator_2);
        indicators = new ImageView[]{zero, one, two};


        slideButton = (Button) findViewById(R.id.slideButton);
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        mViewPager.setCurrentItem(page);
        updateInterface(page);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position >= 0 && position < 2){
                    slideButton.setText("CONTINUE");
                } else {
                    slideButton.setText("GET STARTED");
                }
                page = position;
            }
            
            @Override
            public void onPageScrollStateChanged (int state){}
            
            @Override
            public void onPageSelected(int position) {
                updateInterface(position);
                page = position;
            }

        });

        slideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page += 1;
                if (page <= 2 && page >=0){
                    mViewPager.setCurrentItem(page, true);
                } else {
                    startRegister();
                }
            }
        });



    }

    private void setUpPreferences() {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        editor.putBoolean(Constants.INTRODUCED, true);
        editor.apply();
    }

    private void startRegister() {
        setUpPreferences();
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
        finish();
    }

    private void updateInterface(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(
                    i == position ? R.drawable.indicator_selected : R.drawable.indicator_unselected
            );
        }
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        private String[] titles = {
                "24/7 Access",
                "Quick Response",
                "Direct Messaging"};
        private String[] descriptions = {
                "Get access to cash anytime and anywhere on the go from our app sent directly to your bank account without documentation processes.",
                "Our team is always online to guarantee that you get a response for every loan request in no time.",
                "Send a message directly to the administrative and support team and get a reply within minutes."};
        private int[] icons = {R.drawable.clock_icon,
                                R.drawable.stopwatch_icon,
                                R.drawable.admin_chat_icon};

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int position = getArguments().getInt(ARG_SECTION_NUMBER);
            View rootView = inflater.inflate(R.layout.fragment_intro, container, false);

            TextView titleTextView = (TextView) rootView.findViewById(R.id.titleTextView);
            titleTextView.setText(titles[position]);

            TextView detailTextView = (TextView) rootView.findViewById(R.id.detailsTextView);
            detailTextView.setText(descriptions[position]);

            ImageView slideIcon = (ImageView) rootView.findViewById(R.id.slideIcon);
            slideIcon.setImageResource(icons[position]);


            return rootView;
        }


        private String getButtonText(int position) {
            if(position == 0 || position <= 1){
                return "CONTINUE";
            }
            else {
                return "GET STARTED";
            }

        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }


    }
}
