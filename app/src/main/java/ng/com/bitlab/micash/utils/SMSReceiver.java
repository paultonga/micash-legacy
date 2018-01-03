package ng.com.bitlab.micash.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by Paul on 09/08/2017.
 */

public class SMSReceiver extends BroadcastReceiver {

    private static SMSListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle data = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            String messageBody = smsMessage.getMessageBody();

            if(sender.toLowerCase().contains(Constants.SENDER))
                mListener.messageReceived(getCode(messageBody));

        }
    }

    private String getCode(String messageBody) {
        return messageBody.substring(messageBody.length()-7, messageBody.length()-1);
    }

    public static void bindListener(SMSListener listener){
        mListener = listener;
    }
}
