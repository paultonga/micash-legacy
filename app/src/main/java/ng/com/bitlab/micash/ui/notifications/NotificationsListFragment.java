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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ng.com.bitlab.micash.R;
import ng.com.bitlab.micash.models.Notification;
import ng.com.bitlab.micash.ui.loans.LoansListAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationsListFragment extends Fragment implements NotificationsListContract.View {

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

        //setup Adapter
        List<Notification> temp = new ArrayList<>();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        mAdapter = new NotificationsListAdapter(temp, getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        IntentFilter intentFilter = new IntentFilter("com.ng.bitlab.micash.CUSTOM_EVENT");
        LocalBroadcastManager.getInstance(this.getContext()).registerReceiver(onMessage, intentFilter);

        return mRootView;
    }

    private BroadcastReceiver onMessage = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            mPresenter.loadNotifications();
        }
    };

    @Override
    public void showEmptyLayout() {
        emptyNotificationsLayout.setVisibility(View.VISIBLE);
        mRecyclerView.setVisibility(View.GONE);

    }

    @Override
    public void showNotifications(List<Notification> notifications) {
        emptyNotificationsLayout.setVisibility(View.GONE);
        mRecyclerView.setVisibility(View.VISIBLE);
        mAdapter.refreshData(notifications);
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.loadNotifications();
    }
}
