package ng.com.bitlab.micash.ui.profile;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.AppPreference;
import ng.com.bitlab.micash.core.MiCashApplication;
import ng.com.bitlab.micash.models.Profile;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.ui.addEmployment.AddEmploymentActivity;
import ng.com.bitlab.micash.ui.common.BaseView;

public class ProfileActivity extends BaseView implements ProfileContract.View {

    @BindView(R.id.employment_layout) LinearLayout employmentLayout;

    @BindView(R.id.add_employment_button) Button addEmploymentButton;

    @BindView(R.id.tv_edit_employment) TextView editEmployment;

    @BindView(R.id.iv_recycler_icon) CircleImageView profileImage;
    @BindView(R.id.tv_name) TextView tvName;
    @BindView(R.id.tv_email) TextView tvEmail;
    @BindView(R.id.tv_phone) TextView tvPhone;

    @BindView(R.id.tv_residential) TextView tvResidential;
    @BindView(R.id.tv_state) TextView tvState;
    @BindView(R.id.tv_income) TextView tvIncome;
    @BindView(R.id.tv_office_address) TextView tvAddress;
    @BindView(R.id.tv_office_name) TextView tvOfficeName;
    @BindView(R.id.tv_status) TextView tvStatus;


    AppPreference mPref;
    ProfileContract.Presenter mPresenter;
    private static final int PICK_IMAGE_REQUEST = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ButterKnife.bind(this);
        mPresenter = new ProfilePresenter(this);

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
        mPresenter.getPhoneNumber();
        mPresenter.getProfile();
    }

    @Override
    @OnClick(R.id.add_employment_button)
    public void showAddEmploymentDialog() {
        startActivity(new Intent(this, AddEmploymentActivity.class));
    }

    @Override
    public void showEmptyLayout() {
        employmentLayout.setVisibility(View.GONE);
        addEmploymentButton.setVisibility(View.VISIBLE);
        editEmployment.setVisibility(View.GONE);
    }

    @Override
    public void showDataLayout(Profile pr) {
        if (pr != null) {
            employmentLayout.setVisibility(View.VISIBLE);
            addEmploymentButton.setVisibility(View.GONE);
            editEmployment.setVisibility(View.VISIBLE);

            String income = "\u20a6 " + pr.getIncome();
            tvStatus.setText(pr.getStatus());
            tvOfficeName.setText(pr.getOffice());
            tvAddress.setText(pr.getAddress());
            tvIncome.setText(income);
            tvState.setText(pr.getState());
            tvResidential.setText(pr.getResidential());
        } else {
            showEmptyLayout();
        }

    }

    @Override
    public void setPhoneNumber(String phone) {
        tvPhone.setText(phone);
    }

    @Override
    @OnClick(R.id.iv_recycler_icon)
    public void onProfileImageTouched() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);

    }

    private void startUpload(){
        if(isConnected){
            profileImage.setDrawingCacheEnabled(true);
            profileImage.buildDrawingCache();
            Bitmap bitmap = profileImage.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            mPresenter.uploadProfileImage(data);

        }else {
            showToast("No internet connection");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profileImage.setImageBitmap(bitmap);
                startUpload();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
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
        showEmptyLayout();

        User user = getUserFromPreference();

        tvName.setText(user.getFullName());
        tvEmail.setText(user.getEmail());

        Uri uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();


        Picasso.with(this)
                .load(uri)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(profileImage);
    }

    @Override
    public void refreshImage() {
        Uri uri = FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl();


        Picasso.with(this)
                .load(uri)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .into(profileImage);
    }
}

