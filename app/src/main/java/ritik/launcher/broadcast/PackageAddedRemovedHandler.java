package ritik.launcher.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;

import ritik.launcher.Application;
import ritik.launcher.dataprovider.AppProvider;
import ritik.launcher.utils.UserHandle;

/**
 * This class gets called when an application is created or removed on the
 * system
 * <p/>
 * We then recreate our data set.
 *
 * @author dorvaryn
 */
public class PackageAddedRemovedHandler extends BroadcastReceiver {

	public static void handleEvent(Context ctx, String action, String packageName, UserHandle user, boolean replacing) {
		if (PreferenceManager.getDefaultSharedPreferences(ctx).getBoolean("enable-app-history", true)) {
            // Insert into history new packages (not updated ones)
            if ("android.intent.action.PACKAGE_ADDED".equals(action) && !replacing) {
                // Add new package to history
                Intent launchIntent = ctx.getPackageManager().getLaunchIntentForPackage(packageName);
                if (launchIntent == null) {//for some plugin app
                    return;
                }

				String className = launchIntent.getComponent().getClassName();
				if (className != null) {
					String pojoID = user.addUserSuffixToString("app://" + packageName + "/" + className, '/');
                    Application.getDataHandler(ctx).addToHistory(pojoID);
                }
            }
        }

        if ("android.intent.action.PACKAGE_REMOVED".equals(action) && !replacing) {
            // Removed all installed shortcuts
            Application.getDataHandler(ctx).removeShortcuts(packageName);
            Application.getDataHandler(ctx).removeFromExcluded(packageName, user);
        }

        Application.resetIconsHandler(ctx);

        // Reload application list
        final AppProvider provider = Application.getDataHandler(ctx).getAppProvider();
        if (provider != null) {
            provider.reload();
        }
	}

    @Override
    public void onReceive(Context ctx, Intent intent) {
		handleEvent(ctx,
				intent.getAction(),
				intent.getData().getSchemeSpecificPart(), new UserHandle(),
				intent.getBooleanExtra(Intent.EXTRA_REPLACING, false)
		);

    }

}
