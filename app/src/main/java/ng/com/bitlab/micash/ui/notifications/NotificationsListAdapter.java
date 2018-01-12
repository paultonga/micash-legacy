package ng.com.bitlab.micash.ui.notifications;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.listeners.OnNotificationTouchedListener;
import ng.com.bitlab.micash.models.Notif;

/**
 * Created by Paul on 12/06/2017.
 */

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {

    private List<Notif> mNotifications;
    private final Context mContext;
    private boolean shouldHighlightSelectedItem = false;
    private int selectedPosition = 0;
    private OnNotificationTouchedListener mListener;

    public NotificationsListAdapter(List<Notif> mNotifications, Context mContext, OnNotificationTouchedListener mListener) {
        this.mNotifications = mNotifications;
        this.mContext = mContext;
        this.mListener = mListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rowView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_notification, parent, false);
        NotificationsListAdapter.ViewHolder viewHolder = new NotificationsListAdapter.ViewHolder(rowView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(mNotifications != null) {
            Notif n = mNotifications.get(position);
            holder.title.setText(n.getTitle());
            holder.detail.setText(n.getDescription());
            holder.time.setText(ng.com.bitlab.micash.utils.Formatter.TimeFormatter(n.getCreated()));

            if (n.isRead()) {
                holder.title.setTextColor(Color.parseColor("#696969"));
                holder.detail.setTextColor(Color.parseColor("#696969"));
                //holder.title.setTypeface(holder.detail.getTypeface(), Typeface.NORMAL);
                //holder.detail.setTypeface(holder.detail.getTypeface(), Typeface.NORMAL);
                //holder.time.setTypeface(holder.time.getTypeface(), Typeface.NORMAL);
            } else {
                if (shouldHighlightSelectedItem) {
                    if (selectedPosition == position) {
                        holder.title.setTextColor(Color.parseColor("#696969"));
                        holder.detail.setTextColor(Color.parseColor("#696969"));

                    } else {
                        holder.title.setTextColor(Color.parseColor("#000000"));
                        holder.detail.setTextColor(Color.parseColor("#000000"));

                    }
                } else {
                    holder.title.setTextColor(Color.parseColor("#000000"));
                    holder.detail.setTextColor(Color.parseColor("#000000"));
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return mNotifications == null ? 0 : mNotifications.size();
    }

    public void refreshData(List<Notif> notifications){
        mNotifications = notifications;
        notifyDataSetChanged();
    }



    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.notification_title) TextView title;
        @BindView(R.id.notification_detail) TextView detail;
        @BindView(R.id.notification_time) TextView time;
        public @BindView(R.id.view_foreground) RelativeLayout viewForeground;
        public @BindView(R.id.view_background) RelativeLayout viewBackground;


        public ViewHolder(View itemView) {

            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            shouldHighlightSelectedItem = true;
            selectedPosition = getLayoutPosition();
            mListener.onNotificationTouched(getAtPosition(selectedPosition));
            notifyDataSetChanged();
        }
        public Notif getAtPosition(int position){
            return mNotifications.get(position);
        }
    }
}
