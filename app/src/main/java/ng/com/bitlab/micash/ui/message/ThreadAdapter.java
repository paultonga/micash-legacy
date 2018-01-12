package ng.com.bitlab.micash.ui.message;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.models.Message;
import ng.com.bitlab.micash.utils.Formatter;

/**
 * Created by paul on 1/5/18.
 */

public class ThreadAdapter extends RecyclerView.Adapter<ThreadAdapter.ViewHolder> {

    private static final int VIEW_TYPE_SEMT = 0;
    private static final int VIEW_TYPE_RECEIVED = 1;

    List<Message> messages;

    public ThreadAdapter(List<Message> messages) {
        this.messages = messages;
    }

    public void refreshData(List<Message> messages){
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        String me = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        if(message.getFrom().equals(me)){
            return VIEW_TYPE_SEMT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView;

        switch (viewType){
            case VIEW_TYPE_SEMT:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent,false);
                break;

            case VIEW_TYPE_RECEIVED:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_received, parent,false);
                break;

            default:
                itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_message_sent, parent,false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bodyText.setText(message.getMessage());
        holder.dateText.setText(Formatter.TimeFormatter(message.getDate()));
    }

    @Override
    public int getItemCount() {
        return messages == null ? 0 : messages.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_message_body_text_view) TextView bodyText;
        @BindView(R.id.item_message_date_text_view) TextView dateText;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
