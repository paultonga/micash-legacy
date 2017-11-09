package ng.com.bitlab.micash.ui.loans;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.data.SampleLoansData;
import ng.com.bitlab.micash.listeners.OnLoanSelectedListener;
import ng.com.bitlab.micash.models.Loan;
import ng.com.bitlab.micash.utils.Formatter;

/**
 * Created by Paul on 12/06/2017.
 */

public class LoansListAdapter extends RecyclerView.Adapter<LoansListAdapter.ViewHolder> {


    private List<Loan> mLoans;
    private final Context mContext;
    private boolean shouldHighlightSelectedLoans = false;
    private int selectedPosition = 0;
    private OnLoanSelectedListener mListener;




    public LoansListAdapter(List<Loan> mLoans, Context mContext, OnLoanSelectedListener mListener) {
        this.mLoans = mLoans;
        this.mContext = mContext;
        this.mListener = mListener;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_loan_list, parent, false);
        ViewHolder viewHolder = new ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mLoans != null) {
            Loan loan = mLoans.get(position);
            holder.loan_title.setText(loan.getTitle());
            holder.loan_description.setText(loan.getDescription());
            holder.loan_amount.setText(getAmountText(loan.getMaximum()));
            Picasso.with(mContext)
                    .load(loan.getIcon_url())
                    .fit()
                    .into(holder.loan_icon);

            if (shouldHighlightSelectedLoans){
                if (selectedPosition == position){
                    holder.cardView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.colorPrimaryLight));
                }else {
                    holder.cardView.setBackgroundColor(Color.WHITE);
                }
            }else {
                holder.cardView.setBackgroundColor(Color.WHITE);
            }
        }

    }

    private String getAmountText(double maximum) {
        long amount = ((long) maximum);
        String f = Formatter.numberFormat(amount);
        return "\u20a6" +f+ " maximum obtainable";
    }

    @Override
    public int getItemCount() {
        if(mLoans != null) {
            return mLoans.size();
        } else {
            return 0;
        }
    }

    public void refreshData(List<Loan> loans) {
        mLoans = loans;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.iv_recycler_icon) ImageView loan_icon;
        @BindView(R.id.tv_recycler_title) TextView loan_title;
        @BindView(R.id.tv_recycler_description) TextView loan_description;
        @BindView(R.id.tv_recycler_amount) TextView loan_amount;
        @BindView(R.id.card_view_item_recycler_view) CardView cardView;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            shouldHighlightSelectedLoans = true;
            selectedPosition = getLayoutPosition();
            Loan loan = mLoans.get(selectedPosition);
            notifyDataSetChanged();
            mListener.OnLoanSelected(loan);

        }
    }
}
