package ritik.launcher.loader;

import android.content.Context;

import java.util.ArrayList;

import ritik.launcher.dataprovider.PhoneProvider;
import ritik.launcher.pojo.PhonePojo;

public class LoadPhonePojos extends LoadPojos<PhonePojo> {

    public LoadPhonePojos(Context context) {
        super(context, PhoneProvider.PHONE_SCHEME);
    }

    @Override
    protected ArrayList<PhonePojo> doInBackground(Void... params) {
        return null;
    }
}
