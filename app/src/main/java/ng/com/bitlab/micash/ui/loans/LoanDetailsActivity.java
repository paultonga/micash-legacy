package ng.com.bitlab.micash.ui.loans;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.data.SampleLoansData;
import ng.com.bitlab.micash.models.Interest;
import ng.com.bitlab.micash.models.Loan;
import ng.com.bitlab.micash.utils.Formatter;

public class LoanDetailsActivity extends AppCompatActivity {

    private Loan mLoan;
    private InterestListAdapter mAdapter;
    public String mLoanID = null;

    @BindView(R.id.interests_recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.tv_loan_description) TextView loanDescription;
    @BindView(R.id.tv_recycler_title) TextView loanAmount;
    @BindView(R.id.iv_recycler_icon) ImageView loanIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_details);

        ButterKnife.bind(this);

        //setup adapter
        List<Interest> temp = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        mAdapter = new InterestListAdapter(temp, this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        String title = "Unknown";

        String loan_id;
        Bundle extras = getIntent().getExtras();
        if(extras == null){
            loan_id = null;
        }else {
            loan_id = extras.getString("loan", null);
            mLoanID = loan_id;
        }
        if (loan_id != null){
            mLoan = getLoan(loan_id);
            title = mLoan.getTitle();
            loanDescription.setText(mLoan.getDescription());
            loanAmount.setText(Formatter.numberFormat((long)mLoan.getMaximum()));
            Picasso.with(this)
                    .load(mLoan.getIcon_url())
                    .fit()
                    .into(loanIcon);



        }


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);

        toolbar.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                onBackPressed();
            }
        });
    }

    public Loan getLoan(String loan_id){
        SampleLoansData loans = new SampleLoansData();
        return loans.getLoan(loan_id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SampleLoansData data = new SampleLoansData();
        List<Interest> interests = data.getInterestForLoan(mLoanID);
        mAdapter.refreshData(interests);
    }
}
