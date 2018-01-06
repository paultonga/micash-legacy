package ng.com.bitlab.micash.listeners;

import android.support.annotation.Nullable;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by paul on 1/3/18.
 */

public interface FirebaseQueryListener {

    void onStart();

    void onFinish(@Nullable DataSnapshot dataSnapshot, @Nullable DatabaseError databaseError);
}
