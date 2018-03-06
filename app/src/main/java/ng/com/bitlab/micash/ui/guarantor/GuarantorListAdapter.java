package ng.com.bitlab.micash.ui.guarantor;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.listeners.OnGuaranteeSelectedListener;
import ng.com.bitlab.micash.models.Guarantee;
import ng.com.bitlab.micash.models.Interest;
import ng.com.bitlab.micash.utils.CustomTypefaceSpan;
import ng.com.bitlab.micash.utils.Formatter;

/**
 * Created by Paul Tonga on 21/12/2017.
 */

public class GuarantorListAdapter extends RecyclerView.Adapter<GuarantorListAdapter.ViewHolder> {

    public List<Guarantee> mGuarantees;
    private Context mContext;
    private OnGuaranteeSelectedListener mListener;

    public GuarantorListAdapter(List<Guarantee> mGuarantees, OnGuaranteeSelectedListener mListener, Context mContext) {
        this.mGuarantees = mGuarantees;
        this.mListener = mListener;
        this.mContext = mContext;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_guarantee, parent,false);
        return new GuarantorListAdapter.ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mGuarantees != null){
            Guarantee g = mGuarantees.get(position);

            holder.iv_logo.setImageResource(getIcon(g.isDecided(), g.isApproved()));
            holder.tv_decision.setText(getDecisionText(g.isDecided(), g.isApproved()));
            holder.tv_decision.setBackgroundColor(getDecisionColor(g.isDecided(), g.isApproved()));
            holder.tv_title.setText(getTitleText(g.getRequester_name(), g.getAmount(), g.getInterest()));
            holder.tv_time_ago.setText(Formatter.TimeFormatter(g.getDate_created()));

        }

    }

    private int getIcon(boolean decided, boolean approved) {
        int image = R.drawable.ic_request;

        if(!decided)
            image = R.drawable.ic_request;
        if(decided && approved)
            image = R.drawable.ic_approve;
        if(decided && !approved)
            image = R.drawable.ic_decline;

        return image;
    }

    private int getDecisionColor(boolean decided, boolean approved) {
        int color = Color.parseColor("#2196F3");

        if(!decided)
            color = Color.parseColor("#2196F3");
        if(decided && approved)
            color = Color.parseColor("#4CAF50");
        if(decided && !approved)
            color = Color.parseColor("#f44336");

        return color;
    }

    private SpannableStringBuilder getTitleText(String requester_name, String amount, Interest interest) {

        String amountText = Formatter.getCurrencyText(amount);
        String duration = interest.getMonths() + " month(s).";
        int start = requester_name.length() + 50;
        int end = start +  + amountText.length();
        int ds = end + 16;
        int de = ds + duration.length();

        String s = requester_name + " has requested " +
                "you to guarantee a loan request of " + Formatter.getCurrencyText(amount) +
                " for a tenor of "+duration;
        Typeface boldFont = Typeface.createFromAsset(mContext.getAssets(), "hnbold.ttf");
        SpannableStringBuilder ss = new SpannableStringBuilder(s);
        ss.setSpan(new CustomTypefaceSpan(boldFont), 0, requester_name.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan(boldFont), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new CustomTypefaceSpan(boldFont), ds, de, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        return ss;
    }

    private String getDecisionText(boolean decided, boolean approved) {
        String result = "";
        if(!decided)
            result = "UNDECIDED";
        if(decided && approved)
            result = "APPROVED";
        if(decided && !approved)
            result = "DECLINED";

        return result;
    }

    public void refreshData(List<Guarantee> guarantees){
        mGuarantees = guarantees;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mGuarantees == null ? 0 : mGuarantees.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_guarantor_icon) ImageView iv_logo;
        @BindView(R.id.tv_guarantee_title) TextView tv_title;
        @BindView(R.id.tv_time_ago) TextView tv_time_ago;
        @BindView(R.id.tv_decision) TextView tv_decision;



        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //listener for when user touches one row
            Guarantee guarantee = mGuarantees.get(getLayoutPosition());
            mListener.OnGuaranteeSelected(guarantee);
        }
    }

}
