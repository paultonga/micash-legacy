package ng.com.bitlab.micash.ui.message;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.models.Message;

public class ThreadActivity extends AppCompatActivity implements ThreadContract.View {

    @BindView(R.id.activity_thread_empty_view) TextView emptyThreadTextView;
    @BindView(R.id.activity_thread_messages_recycler) RecyclerView mRecycler;
    @BindView(R.id.activity_thread_progress) ProgressBar progressBar;
    @BindView(R.id.activity_thread_input_edit_text) TextView inputMessage;

    ThreadContract.Presenter mPresenter;
    ThreadAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        ButterKnife.bind(this);
        mPresenter = new ThreadPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Customer Support");

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        List<Message> temp = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new ThreadAdapter(temp);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter);


        mPresenter.loadMessages();
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyText() {
        mRecycler.setVisibility(View.GONE);
        emptyThreadTextView.setVisibility(View.VISIBLE);
    }

    @Override
    public void clearTextInput() {
        inputMessage.setText("");
        mRecycler.getAdapter().registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                mRecycler.smoothScrollToPosition(0);
            }
        });
    }

    @Override
    @OnClick(R.id.activity_thread_send_fab)
    public void sendMessage() {
        if(inputMessage.getText().length() > 0){
            Message message = new Message(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                    inputMessage.getText().toString(), DateTime.now().getMillis());
            mPresenter.sendMessage(message);
        }
    }

    @Override
    public void showMessages(List<Message> messages) {
        emptyThreadTextView.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        mAdapter.refreshData(messages);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadMessages();
    }
}
