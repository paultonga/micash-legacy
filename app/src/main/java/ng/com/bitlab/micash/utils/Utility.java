package ng.com.bitlab.micash.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

/**
 * Created by paul on 1/5/18.
 */

public class Utility {

    public static void updateInstanceID(){
        FirebaseUser u  = FirebaseAuth.getInstance().getCurrentUser();
        if(u != null) {

            FirebaseDatabase.getInstance().getReference()
                    .child("fcmTokens")
                    .child(u.getUid())
                    .child("token")
                    .setValue(FirebaseInstanceId.getInstance().getToken());
        }
    }
}
