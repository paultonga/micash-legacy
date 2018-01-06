package ng.com.bitlab.micash.ui.message;

import java.util.List;

import ng.com.bitlab.micash.listeners.FirebaseQueryListener;
import ng.com.bitlab.micash.models.Message;

/**
 * Created by paul on 1/5/18.
 */

public interface ThreadContract {

    interface View{
        void showProgress();

        void hideProgress();

        void showEmptyText();

        void showMessages(List<Message> messages);

        void sendMessage();

        void clearTextInput();
    }

    interface Presenter {

        void loadMessages();



        void sendMessage(Message message);

    }

    interface Repository {

        void postMessage(Message message, String uuid, FirebaseQueryListener listener);

        void fetchMessages(String uuid, FirebaseQueryListener listener);

    }
}
