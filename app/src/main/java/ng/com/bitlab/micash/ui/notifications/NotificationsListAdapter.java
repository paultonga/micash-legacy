package ng.com.bitlab.micash.ui.notifications;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Formatter;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.models.Notification;
import ng.com.bitlab.micash.ui.loans.LoansListAdapter;

/**
 * Created by Paul on 12/06/2017.
 */

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {

    private List<Notification> mNotifications;
    private final Context mContext;
    private boolean shouldHighlightSelectedItem = false;
    private int selectedPosition = 0;

    public NotificationsListAdapter(List<Notification> mNotifications, Context mContext) {
        this.mNotifications = mNotifications;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false);
        NotificationsListAdapter.ViewHolder viewHolder = new NotificationsListAdapter.ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(mNotifications != null){
            Notification n = mNotifications.get(position);
            if(!n.isRead()){
                holder.title.setText(n.getTitle());
                holder.title.setTypeface(holder.title.getTypeface(), Typeface.BOLD);

                holder.detail.setText(n.getDetail());
                holder.detail.setTypeface(holder.detail.getTypeface(), Typeface.BOLD);

                holder.time.setText(ng.com.bitlab.micash.utils.Formatter.TimeFormatter(n.getDateSent()));
                holder.time.setTypeface(holder.time.getTypeface(), Typeface.BOLD);
            } else {
                holder.title.setText(n.getTitle());
                holder.title.setTypeface(holder.detail.getTypeface(), Typeface.NORMAL);

                //holder.title.setTextColor(Color.parseColor("#A9A9A9"));

                holder.detail.setText(n.getDetail());
                holder.detail.setTypeface(holder.detail.getTypeface(), Typeface.NORMAL);

                holder.time.setText(ng.com.bitlab.micash.utils.Formatter.TimeFormatter(n.getDateSent()));
                holder.time.setTypeface(holder.time.getTypeface(), Typeface.NORMAL);

            }

        }

    }

    @Override
    public int getItemCount() {
        return mNotifications == null ? 0 : mNotifications.size();
    }

    public void refreshData(List<Notification> notifications){
        mNotifications = notifications;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.notification_title) TextView title;
        @BindView(R.id.notification_detail) TextView detail;
        @BindView(R.id.notification_time) TextView time;

        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            shouldHighlightSelectedItem = true;
            selectedPosition = getLayoutPosition();
            Notification n = mNotifications.get(selectedPosition);
            n.setRead(true);
            n.save();
            notifyDataSetChanged();

        }
    }
}
