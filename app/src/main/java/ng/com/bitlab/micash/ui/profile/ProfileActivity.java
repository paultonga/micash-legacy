package ng.com.bitlab.micash.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.ui.addContact.AddContactActivity;
import ng.com.bitlab.micash.ui.addEmployment.AddEmploymentActivity;
import ng.com.bitlab.micash.ui.addPersonalDetails.addPersonalDetailsDialog;

public class ProfileActivity extends AppCompatActivity implements ProfileContract.View {

    @BindView(R.id.employment_layout) LinearLayout employmentLayout;
    @BindView(R.id.contact_layout) LinearLayout contactLayout;

    @BindView(R.id.add_contact_button) Button addContactButton;
    @BindView(R.id.add_employment_button) Button addEmploymentButton;

    @BindView(R.id.tv_edit_contact) TextView editContact;
    @BindView(R.id.tv_edit_employment) TextView editEmployment;

    @BindView(R.id.iv_recycler_icon) CircleImageView profileImage;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_email) TextView tvEmail;
    @BindView(R.id.tv_phone) TextView tvPhone;

    AppPreference mPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Profile");

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        mPref = MiCashApplication.getPreference();
        initLayout();
    }

    @Override
    @OnClick(R.id.add_employment_button)
    public void showAddEmplymentDialog() {
        //addPersonalDetailsDialog d = new addPersonalDetailsDialog();
        //d.show(getSupportFragmentManager(), "Dialog");
        startActivity(new Intent(this, AddEmploymentActivity.class));
    }

    @Override
    @OnClick(R.id.add_contact_button)
    public void showAddContactDialog() {
        startActivity(new Intent(this, AddContactActivity.class));
    }

    public User getUserFromPreference() {

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String user = MiCashApplication.getPreference().getUser();
        if (user != null){
            Gson gson = new Gson();
            User u = gson.fromJson(user, User.class);
            return u;
        }
        return null;
    }

    private void initLayout(){
        if(mPref.getEmploymentSaved() == null){
            employmentLayout.setVisibility(View.GONE);
            addEmploymentButton.setVisibility(View.VISIBLE);
            editEmployment.setVisibility(View.GONE);
        }

        if(mPref.getContactSaved() == null){
            contactLayout.setVisibility(View.GONE);
            addContactButton.setVisibility(View.VISIBLE);
            editContact.setVisibility(View.GONE);
        }

        User user = getUserFromPreference();

        tvName.setText(user.getFullName());
        tvEmail.setText(user.getEmail());
        tvPhone.setText(mPref.getPhone());

        Picasso.with(this)
                .load(Uri.parse(user.getProfileImage()))
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(profileImage);
    }
    }

