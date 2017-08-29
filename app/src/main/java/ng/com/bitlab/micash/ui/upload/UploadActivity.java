package ng.com.bitlab.micash.ui.upload;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.common.MainActivity;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.utils.Constants;

public class UploadActivity extends BaseView implements
                UploadContract.View {

    private static final int PICK_IMAGE_REQUEST = 1;
    Drawable defaultImage;
    UploadContract.Presenter mPresenter;


    @BindView(R.id.profile_image) CircleImageView profileImage;
    @BindView(R.id.btn_set_profile_image) Button btnSaveProfileImage;
    @BindView(R.id.tv_skip) TextView btnSkip;
    @BindView(R.id.btn_get_started) Button btnGetStarted;

    @BindView(R.id.completed_layout) RelativeLayout completedLayout;
    @BindView(R.id.profile_image_layout) RelativeLayout profileImageLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        ButterKnife.bind(this);

        mPresenter = new UploadPresenter(this);



        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        String appState = sp.getString(Constants.APP_STATE, null);

        if (appState != null){
            switch (appState){
                case Constants.GET_STARTED_VIEW:
                    showGetStartedLayout();
                    break;
                case Constants.START_UPLOAD_IMAGE:
                    showImageUploadLayout();
                    break;
                default:
                    showImageUploadLayout();
            }
        }else {
            showImageUploadLayout();
        }



    }

    @Override
    @OnClick(R.id.btn_set_profile_image)
    public void uploadButtonClicked() {

        if(isConnected) {

            if (profileImage.getDrawable() != defaultImage) {
                profileImage.setDrawingCacheEnabled(true);
                profileImage.buildDrawingCache();
                Bitmap bitmap = profileImage.getDrawingCache();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();

                mPresenter.uploadProfileImage(data);
            } else {
                showSnackBar("Please select an image");
            }
        } else {
            showSnackBar("There's no internet connection.");
        }

    }

    @Override
    public void showImageUploadLayout(){
        savePreferences(Constants.START_UPLOAD_IMAGE);
        profileImageLayout.setVisibility(View.VISIBLE);
        completedLayout.setVisibility(View.GONE);

        defaultImage = profileImage.getDrawable();

    }

    @Override
    @OnClick(R.id.btn_get_started)
    public void getStartedButtonClicked() {
        if(isConnected) {
            mPresenter.saveUser();
        } else {
            showSnackBar("No internet connection.");
        }
    }

    @Override
    public void startMainActivity(){
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void savePreferences(String s) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sp.edit();

        editor.putString(Constants.APP_STATE, s);
        editor.apply();
    }

    @Override
    @OnClick(R.id.tv_skip)
    public void showGetStartedLayout() {
        savePreferences(Constants.GET_STARTED_VIEW);
        completedLayout.setVisibility(View.VISIBLE);
        profileImageLayout.setVisibility(View.GONE);

    }

    @Override
    @OnClick(R.id.profile_image)
    public void selectProfileImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
        //finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri uri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                profileImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
