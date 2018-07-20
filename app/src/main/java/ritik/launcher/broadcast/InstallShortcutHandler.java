package ritik.launcher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.graphics.Bitmap;
import android.util.Log;

import java.net.URISyntaxException;

import ritik.launcher.DataHandler;
import ritik.launcher.Application;
import ritik.launcher.dataprovider.ShortcutsProvider;
import ritik.launcher.pojo.ShortcutsPojo;

public class InstallShortcutHandler extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent data) {

        DataHandler dh = Application.getDataHandler(context);
        ShortcutsProvider sp = dh.getShortcutsProvider();

        if (sp == null)
            return;

        String name = data.getStringExtra(Intent.EXTRA_SHORTCUT_NAME);
        Log.d("onReceive", "Received shortcut " + name);

        Intent target = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_INTENT);
        if (target.getAction() == null) {
            target.setAction(Intent.ACTION_VIEW);
        }

        ShortcutsPojo pojo = createPojo(name);

        // convert target intent to parsable uri
        pojo.intentUri = target.toUri(0);

        //get embedded icon
        Bitmap icon = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON);
        if (icon != null) {
            Log.d("onReceive", "Shortcut " + name + " has embedded icon");
            pojo.icon = icon;
        } else {
            ShortcutIconResource sir = data.getParcelableExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE);

            if (sir != null) {
                Log.d("onReceive", "Received icon package name " + sir.packageName);
                Log.d("onReceive", "Received icon resource name " + sir.resourceName);

                pojo.packageName = sir.packageName;
                pojo.resourceName = sir.resourceName;
            } else {
                //invalid shortcut
                Log.d("onReceive", "Invalid shortcut " + name + ", ignoring");
                return;
            }
        }

        try {
            Intent intent = Intent.parseUri(pojo.intentUri, 0);
            if (intent.getCategories() != null && intent.getCategories().contains(Intent.CATEGORY_LAUNCHER) && intent.getAction().equals(Intent.ACTION_MAIN)) {
                // The Play Store has an option to create shortcut for new apps,
                // However, launcher already displays all apps, so we discard the shortcut to avoid duplicates.
                Log.d("onReceive", "Shortcut for launcher app, discarded.");
                return;
            }
        } catch (URISyntaxException e) {
            // Invalid intentUri: skip
            // (should logically not happen)
            e.printStackTrace();
            return;
        }

        dh.addShortcut(pojo);
    }

    public ShortcutsPojo createPojo(String name) {
        ShortcutsPojo pojo = new ShortcutsPojo();

        pojo.id = ShortcutsPojo.SCHEME + name.toLowerCase();
        pojo.setName(name);

        return pojo;
    }

}
