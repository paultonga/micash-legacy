package ng.com.bitlab.micash.ui.transactions;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.models.Request;
import ng.com.bitlab.micash.ui.common.BaseView;
import ng.com.bitlab.micash.utils.TransactionsItemTouchHelper;

public class TransactionsActivity extends BaseView implements TransactionsContract.View, TransactionsItemTouchHelper.TransactionItemTouchHelperListener {

    TransactionsAdapter mAdapter;
    TransactionsContract.Presenter mPresenter;

    @BindView(R.id.empty_transaction_list) RelativeLayout emptyLayout;
    @BindView(R.id.transaction_loading_layout) RelativeLayout loadingLayout;
    @BindView(R.id.transaction_list_recyclerview) RecyclerView mRecycler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactions);
        ButterKnife.bind(this);
        mPresenter = new TransactionsPresenter(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Transactions");

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        List<Request> temp = new ArrayList<>();
        mAdapter = new TransactionsAdapter(temp, this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter);

        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new TransactionsItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecycler);

        mPresenter.loadTransactions();

    }

    @Override
    public void showEmptyLayout() {
        emptyLayout.setVisibility(View.VISIBLE);
        loadingLayout.setVisibility(View.GONE);
        mRecycler.setVisibility(View.GONE);
    }

    @Override
    public void showTransactionList(List<Request> requests) {
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        mAdapter.refreshData(requests);
    }

    @Override
    public void showLoadingLayout() {
        emptyLayout.setVisibility(View.GONE);
        loadingLayout.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof TransactionsAdapter.ViewHolder){
            Request r = ((TransactionsAdapter.ViewHolder) viewHolder).getRecordAt(position);
            mPresenter.deleteTransaction(r);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadTransactions();
    }
}
