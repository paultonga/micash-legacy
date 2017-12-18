package ng.com.bitlab.micash.ui.cards;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.models.AccountRecord;
import ng.com.bitlab.micash.models.BankRecord;

/**
 * Created by paul on 12/3/17.
 */

public class CardsListAdapter extends RecyclerView.Adapter<CardsListAdapter.ViewHolder> {

    private List<BankRecord> mCards;

    public CardsListAdapter(List<BankRecord> mCards) {
        this.mCards = mCards;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_cards_list, parent,false);
        return new CardsListAdapter.ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mCards != null){
            BankRecord record = mCards.get(position);
            holder.bankName.setText(record.getName());
            holder.bankAccount.setText(record.getNumber());
        }

    }

    @Override
    public int getItemCount() {
        return mCards == null ? 0 : mCards.size();
    }

    public void refreshData(List<BankRecord> r){
        mCards = r;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.bank_name) TextView bankName;
        @BindView(R.id.account_number) TextView bankAccount;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
