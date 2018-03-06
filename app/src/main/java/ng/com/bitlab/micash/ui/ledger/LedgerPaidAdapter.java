package ng.com.bitlab.micash.ui.ledger;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.models.Ledger;
import ng.com.bitlab.micash.utils.Constants;
import ng.com.bitlab.micash.utils.CustomTypefaceSpan;
import ng.com.bitlab.micash.utils.Formatter;

/**
 * Created by Paul Tonga on 10/02/2018.
 */

public class LedgerPaidAdapter extends RecyclerView.Adapter<LedgerPaidAdapter.ViewHolder> {

    List<Ledger> mLedgers;
    Context mContext;
    public LedgerPaidAdapter(List<Ledger> mLedgers, Context context) {
        this.mLedgers = mLedgers;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_ledger, parent, false);
        return new ViewHolder(rowView);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mLedgers != null){
            Ledger l = mLedgers.get(position);
            holder.ledgerTitle.setText(getTitle(l.getAmount()));
            holder.ledgerDetail.setText(getDesription(l.getDatePaid()));
            holder.ledgerStatus.setText("PAID");
            holder.ledgerStatus.setBackgroundColor(Color.parseColor("#4CAF50"));
            String requested = Constants.naira + l.getRequestedAmount();
            holder.requestedAmount.setText(requested);
        }
    }

    private SpannableStringBuilder getDesription(long datePaid){
        String text = "This payment was received on  ";
        DateTime due = new DateTime(datePaid);
        DateTimeFormatter dtf = DateTimeFormat.forPattern("MMMM d, yyyy");
        String output = text + due.toString(dtf) + ".";
        int start = text.length();
        int end = output.length();

        Typeface boldFont = Typeface.createFromAsset(mContext.getAssets(), "hnbold.ttf");
        SpannableStringBuilder ss = new SpannableStringBuilder(output);
        ss.setSpan(new CustomTypefaceSpan(boldFont), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return ss;
    }

    private String getRequestedAmount(String s){
        return Constants.naira + s;
    }

    private String getTitle(long amount){
        DecimalFormat numberFormat = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.US);
        DecimalFormatSymbols symbols = numberFormat.getDecimalFormatSymbols();
        symbols.setCurrencySymbol("");
        numberFormat.setDecimalFormatSymbols(symbols);
        numberFormat.setMinimumFractionDigits(0);

        return Constants.naira + numberFormat.format(amount) + " Payback";
    }


    @Override
    public int getItemCount() {
        return mLedgers == null ? 0 : mLedgers.size();
    }

    public void refresh(List<Ledger> ledgers){
        mLedgers = ledgers;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.ledger_title)
        TextView ledgerTitle;
        @BindView(R.id.ledger_detail) TextView ledgerDetail;
        @BindView(R.id.ledger_status) TextView ledgerStatus;
        @BindView(R.id.requested_amount) TextView requestedAmount;
        public @BindView(R.id.view_foreground)
        RelativeLayout viewForeground;



        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public Ledger getAtPosition(int position){
            return mLedgers.get(position);
        }
    }
}
