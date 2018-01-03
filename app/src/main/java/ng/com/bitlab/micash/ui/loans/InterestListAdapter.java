package ng.com.bitlab.micash.ui.loans;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.models.Interest;

/**
 * Created by Paul on 10/10/2017.
 */

public class InterestListAdapter extends RecyclerView.Adapter<InterestListAdapter.ViewHolder> {

    private List<Interest> mInterests;
    private Context mContext;

    public InterestListAdapter(List<Interest> mInterests, Context mContext) {
        this.mInterests = mInterests;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_interest_list, parent, false);
        return new ViewHolder(rowView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mInterests != null){
            Interest i =  mInterests.get(position);
            holder.interestDescription.setText(i.getDescription());
        }
    }

    public void refreshData(List<Interest> interests){
        mInterests = interests;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(mInterests != null){
            return mInterests.size();
        } else {
            return 0;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_interest_description) TextView interestDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
