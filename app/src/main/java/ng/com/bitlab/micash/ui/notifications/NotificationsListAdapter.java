package ng.com.bitlab.micash.ui.notifications;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ng.com.bitlab.micash.models.Notification;

/**
 * Created by Paul on 12/06/2017.
 */

public class NotificationsListAdapter extends RecyclerView.Adapter<NotificationsListAdapter.ViewHolder> {

    private List<Notification> mNotifications;
    private final Context mContext;

    public NotificationsListAdapter(List<Notification> mNotifications, Context mContext) {
        this.mNotifications = mNotifications;
        this.mContext = mContext;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public void onClick(View view) {

        }
    }
}
