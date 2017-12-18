package ng.com.bitlab.micash.listeners;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;

/**
 * Created by paul on 12/16/17.
 */

public interface FirebaseDataListener {

    void onStart();
    void onSuccess(DataSnapshot ds);
    void onFailed(DatabaseError de);
    void onComplete(DatabaseError de);
}
