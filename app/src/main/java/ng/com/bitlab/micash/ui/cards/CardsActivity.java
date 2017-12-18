package ng.com.bitlab.micash.ui.cards;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.models.AccountRecord;
import ng.com.bitlab.micash.models.BankRecord;
import ng.com.bitlab.micash.ui.addBanking.AddBankingActivity;

public class CardsActivity extends AppCompatActivity implements CardsContract.View {

    @BindView(R.id.add_banking_button) Button addBankingButton;
    @BindView(R.id.empty_bank_list) RelativeLayout emptyLayout;
    @BindView(R.id.bank_list_recyclerview) RecyclerView mRecycler;

    CardsListAdapter mAdapter;
    CardsContract.Presenter mPresenter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        ButterKnife.bind(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Banking Details");

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });

        mPresenter = new CardsPresenter(this);

        List<BankRecord> temp = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new CardsListAdapter(temp);
        mRecycler.setLayoutManager(layoutManager);
        mRecycler.setAdapter(mAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_add){
            Toast.makeText(this, "This will add new bank details", Toast.LENGTH_LONG).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @OnClick(R.id.add_banking_button)
    public void onAddButtonClicked(){
        startActivity(new Intent(this, AddBankingActivity.class));
    }

    @Override
    public void showEmptyLayout() {
        emptyLayout.setVisibility(View.VISIBLE);
        mRecycler.setVisibility(View.GONE);
    }

    @Override
    public void showCards(List<BankRecord> records) {
        emptyLayout.setVisibility(View.GONE);
        mRecycler.setVisibility(View.VISIBLE);
        mAdapter.refreshData(records);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.loadData();
    }
}
