package ng.com.bitlab.micash.ui.notifications;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.otto.Subscribe;


import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.events.NotificationEvent;
import ng.com.bitlab.micash.listeners.NotificationAddedListener;
import ng.com.bitlab.micash.listeners.OnNotificationTouchedListener;
import ng.com.bitlab.micash.models.Notif;
import ng.com.bitlab.micash.ui.common.BaseFragment;
import ng.com.bitlab.micash.ui.guarantor.GuarantorActivity;
import ng.com.bitlab.micash.ui.message.ThreadActivity;
import ng.com.bitlab.micash.ui.transactions.TransactionsActivity;
import ng.com.bitlab.micash.utils.NotificationRecyclerItemTH;

import static ng.com.bitlab.micash.core.MiCashApplication.bus;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsListFragment extends BaseFragment implements OnNotificationTouchedListener,
        NotificationsListContract.View,
        NotificationRecyclerItemTH.NotificationRecyclerItemTHListener {

    NotificationsListContract.Presenter mPresenter;
    View mRootView;
    NotificationsListAdapter mAdapter;

    @BindView(R.id.notifications_list_recyclerview) RecyclerView mRecyclerView;
    @BindView(R.id.empty_notifications_list) RelativeLayout emptyNotificationsLayout;



    public NotificationsListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.fragment_notifications_list, container, false);
        ButterKnife.bind(this, mRootView);

        mPresenter = new NotificationsListPresenter(this);
        bus.register(this);

        //setup Adapter
        List<Notif> temp = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new NotificationsListAdapter(temp,this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);


        ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new NotificationRecyclerItemTH(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);

        mPresenter.loadNotifications();

        return mRootView;
    }

    @Override
    public void showEmptyLayout() {
        emptyNotificationsLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);
    }

    @Override
    public void showLoadingLayout() {

    }


    @Override
    public void showNotifications(List<Notif> notifications) {
        emptyNotificationsLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        Collections.reverse(notifications);
        mAdapter.refreshData(notifications);
    }

    @Override
    public void onNotificationReceived(Notif notif) {
        mPresenter.loadNotifications();
    }

    @Override
    public void onClearAllClicked() {

    }

    @Override
    public void showClearAllWarning() {

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadNotifications();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof NotificationsListAdapter.ViewHolder){
            Notif n = ((NotificationsListAdapter.ViewHolder) viewHolder).getAtPosition(position);
            mPresenter.delete(n);
        }
    }

    @Override
    public void onNotificationTouched(Notif n) {
        mPresenter.setIsRead(n);
        if(n.getTitle().contains("Guarantor Request Received")){
            startActivity(new Intent(getContext(), GuarantorActivity.class));
        }
        else if(n.getTitle().contains("Guarantor Request Declined")){
            startActivity(new Intent(getContext(), TransactionsActivity.class));
        }
        else if(n.getTitle().contains("Message")){
            startActivity(new Intent(getContext(), ThreadActivity.class));
        }else {
            //do nothing

        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

}
