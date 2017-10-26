package ng.com.bitlab.micash.ui.processing;

import com.google.firebase.auth.FirebaseUser;

import ng.com.bitlab.micash.models.Device;
import ng.com.bitlab.micash.models.User;
import ng.com.bitlab.micash.models.Verify;

/**
 * Created by Paul on 04/10/2017.
 */

public interface ProcessingContract {

    interface View {
        void showErrorLayout(String error);

        void showProcessingLayout();

        void updateMessage(String title, String message);

        void startUploadActivity();


    }

    interface Presenter{

        void createUserAccount();

        void saveUsertoFirebase(FirebaseUser firebaseUser);

        void finalizeSetup();

        User getUser();

        Device getDevice();

        Verify getVerify();

    }
}
