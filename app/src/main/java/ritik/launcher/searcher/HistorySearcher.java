package ritik.launcher.searcher;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.List;

import ritik.launcher.Application;
import ritik.launcher.MainActivity;
import ritik.launcher.pojo.Pojo;

/**
 * Retrieve pojos from history
 */
public class HistorySearcher extends Searcher {
    private SharedPreferences prefs;

    public HistorySearcher(MainActivity activity) {
        super(activity);
        prefs = PreferenceManager.getDefaultSharedPreferences(activity);
    }

    @Override
    protected List<Pojo> doInBackground(Void... voids) {
        // Ask for records
        boolean smartHistory = !prefs.getString("history-mode", "recency").equals("recency");
        boolean excludeFavorites = prefs.getBoolean("exclude-favorites", false);
        
        // Convert `"number-of-display-elements"` to double first before truncating to int to avoid
        // `java.lang.NumberFormatException` crashes for values larger than `Integer.MAX_VALUE`
        int maxRecords = (Double.valueOf(prefs.getString("number-of-display-elements", String.valueOf(DEFAULT_MAX_RESULTS)))).intValue();

        //Gather favorites
        ArrayList<Pojo> favoritesPojo = new ArrayList<Pojo>(0);
        if(excludeFavorites){
            favoritesPojo = Application.getDataHandler(activity).getFavorites(activity.tryToRetrieve);
        }

        return Application.getDataHandler(activity).getHistory(activity, maxRecords, smartHistory, favoritesPojo);
    }
}
