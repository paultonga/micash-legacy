package ng.com.bitlab.micash.ui.message;

import android.support.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

import java.util.ArrayList;
import java.util.List;

import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
import ng.com.bitlab.micash.models.Message;

/**
 * Created by paul on 1/5/18.
 */

public class ThreadPresenter implements ThreadContract.Presenter {

    private ThreadContract.View mView;
    private ThreadContract.Repository mRepository;

    public ThreadPresenter(ThreadContract.View mView) {
        this.mView = mView;
        mRepository = new ThreadRepository();
    }

    @Override
    public void loadMessages() {

        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        mRepository.fetchMessages(uuid, new FirebaseQueryListener() {
            @Override
            public void onStart() {
                mView.showProgress();
            }

            @Override
            public void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError) {
                mView.hideProgress();
                if(databaseError == null){
                    if(dataSnapshot.exists()){
                        List<Message> messages = new ArrayList<>();
                        for(DataSnapshot ds : dataSnapshot.getChildren()){
                            Message m = ds.getValue(Message.class);
                            messages.add(m);
                        }
                        mView.showMessages(messages);

                    } else {
                        mView.showEmptyText();
                    }
                }
            }
        });



    }

    @Override
    public void sendMessage(Message message) {
        String uuid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mRepository.postMessage(message, uuid, new FirebaseQueryListener() {
            @Override
            public void onStart() {
                mView.showProgress();
            }

            @Override
            public void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError) {
                if(databaseError == null){
                    mView.hideProgress();
                    mView.clearTextInput();
                    loadMessages();
                }
            }
        });



    }
}
