package ng.com.bitlab.micash.ui.transactions;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Formatter;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.models.Request;
import ng.com.bitlab.micash.utils.CustomTypefaceSpan;

/**
 * Created by paul on 1/6/18.
 */

public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder> {

    private List<Request> mRecords;
    private Context mContext;

    public TransactionsAdapter(List<Request> mRecords, Context mContext) {
        this.mRecords = mRecords;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_transactions, parent,false);
        return new TransactionsAdapter.ViewHolder(rowView);
    }


    public void refreshData(List<Request> records){
        mRecords = records;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mRecords != null){
            Request r = mRecords.get(position);
            holder.tv_time_ago.setText(ng.com.bitlab.micash.utils.Formatter.TimeFormatter(r.getDate_created()));
            holder.tv_status.setText(getStatus(r));
            holder.tv_title.setText(getTitle(r));
        }
    }

    private SpannableStringBuilder getTitle(Request r){
        String amountText = ng.com.bitlab.micash.utils.Formatter.getCurrencyText(r.getAmount());
        String loanTitle = r.getLoan().getTitle();
        int start = 18 + loanTitle.length() + 4;
        int end = start + amountText.length();

        String s = "You requested for " + loanTitle +" of " + amountText +".";

        Typeface boldFont = Typeface.createFromAsset(mContext.getAssets(), "hnbold.ttf");
        SpannableStringBuilder ss = new SpannableStringBuilder(s);
        ss.setSpan(new CustomTypefaceSpan(boldFont), 18, loanTitle.length()+18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan(boldFont), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return ss;
    }

    private String getStatus(Request r){
        String status = "";
        if (r.isDecided() && !r.isApproved()){
            status = "LOAN DECLINED";
        }
        if(!r.isDecided() && !r.isApproved()){
            status = "GUARANTOR DECLINED";
        }
        return status;
    }

    @Override
    public int getItemCount() {
        return mRecords == null ? 0 : mRecords.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_transaction_title) TextView tv_title;
        @BindView(R.id.tv_time_ago) TextView tv_time_ago;
        @BindView(R.id.tv_status) TextView tv_status;
        public @BindView(R.id.view_foreground) RelativeLayout viewForeground;
        public @BindView(R.id.view_background) RelativeLayout viewBackground;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public Request getRecordAt(int position){
            return mRecords.get(position);
        }
    }
}
