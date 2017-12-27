package ng.com.bitlab.micash.ui.guarantor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.listeners.OnGuaranteeSelectedListener;
import ng.com.bitlab.micash.models.Guarantee;

public class GuarantorActivity extends AppCompatActivity implements GuarantorContract.View, OnGuaranteeSelectedListener {

    @BindView(R.id.empty_layout) RelativeLayout emptyLayout;
    @BindView(R.id.loading_layout) RelativeLayout loadingLayout;
    @BindView(R.id.recycler_view) RecyclerView mRecycler;

    GuarantorListAdapter mAdapter;
    GuarantorContract.Presenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guarantor);
        ButterKnife.bind(this);

        mPresenter = new GuarantorPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Guarantor Requests");

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        List<Guarantee> temp = new ArrayList<>();
        RecyclerView.LayoutManager lm = new LinearLayoutManager(this);
        mAdapter = new GuarantorListAdapter(temp);
        mRecycler.setLayoutManager(lm);
        mRecycler.setAdapter(mAdapter);
    }

    @Override
    public void showLoadingLayout() {
        loadingLayout.setVisibility(View.VISIBLE);
        emptyLayout.setVisibility(View.GONE);
        mRecycler.setVisibility(View.GONE);
    }

    @Override
    public void showEmptyDataLayout() {
        emptyLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        mRecycler.setVisibility(View.GONE);
    }

    @Override
    public void showRecyclerView(List<Guarantee> guarantees) {
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        mAdapter.refreshData(guarantees);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.fetchGuarantorRequests();
    }

    @Override
    public void OnGuaranteeSelected(Guarantee guarantee) {
        //show dialog to accept or decline guarantor request
    }
}
