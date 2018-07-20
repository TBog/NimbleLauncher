package ritik.launcher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import ritik.launcher.DataHandler;
import ritik.launcher.Application;
import ritik.launcher.dataprovider.ContactsProvider;
import ritik.launcher.pojo.ContactsPojo;

public class IncomingSmsHandler extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Only handle SMS received
        if (!intent.getAction().equals("android.provider.Telephony.SMS_RECEIVED")) {
            return;
        }

        // Stop if contacts are not enabled
        DataHandler dataHandler = Application.getDataHandler(context);
        ContactsProvider contactsProvider = dataHandler.getContactsProvider();
        if (contactsProvider == null) {
            // Contacts have been disabled from settings
            return;
        }

        // Get the SMS message passed in, if any
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }

        // Retrieve the SMS message received.
        // Since we're not interested in content, we can safely discard
        // all records but the first one
        Object[] pdus = (Object[]) bundle.get("pdus");
        if (pdus == null) {
            return;
        }
        SmsMessage msg = SmsMessage.createFromPdu((byte[]) pdus[0]);

        // Now, retrieve the contact by its lookup key on our contactsProvider
        ContactsPojo contactPojo = contactsProvider.findByPhone(msg.getOriginatingAddress());
        if (contactPojo != null) {
            // We have a match!
            dataHandler.addToHistory(contactPojo.id);
        }
    }
}
