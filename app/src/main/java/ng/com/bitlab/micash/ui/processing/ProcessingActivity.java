package ng.com.bitlab.micash.ui.processing;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.ui.upload.UploadActivity;

public class ProcessingActivity extends AppCompatActivity implements
    ProcessingContract.View {

    ProcessingContract.Presenter mPresenter;
    @BindView(R.id.processing_detail) TextView txtMessage;
    @BindView(R.id.processing_title) TextView txtTitle;
    @BindView(R.id.error_detail) TextView txtError;

    @BindView(R.id.processing_layout) RelativeLayout processingLayout;
    @BindView(R.id.error_layout) RelativeLayout errorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_processing);

        ButterKnife.bind(this);

        mPresenter = new ProcessingPresenter(this);
        mPresenter.createUserAccount();
    }

    @Override
    public void showErrorLayout(String error) {
        processingLayout.setVisibility(View.GONE);
        errorLayout.setVisibility(View.VISIBLE);
        txtError.setText(error);
    }

    @Override
    public void showProcessingLayout() {
        errorLayout.setVisibility(View.GONE);
        processingLayout.setVisibility(View.VISIBLE);
    }

    @Override
    public void startUploadActivity() {
        Intent i = new Intent(this, UploadActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    @Override
    public void updateMessage(String title, String message) {
        txtMessage.setText(message);
        txtTitle.setText(title);
    }
}
