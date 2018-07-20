package ritik.launcher.searcher;

import android.os.AsyncTask;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ritik.launcher.Application;
import ritik.launcher.MainActivity;
import ritik.launcher.pojo.Pojo;
import ritik.launcher.result.Result;

/**
 * Returns the list of all applications on the system
 */
public class ApplicationsSearcher  extends AsyncTask<Void, Void, List<Pojo>> {
    final MainActivity activity;
    public ApplicationsSearcher(MainActivity activity) {
        super();
        this.activity = activity;

    }


    @Override
    protected List<Pojo> doInBackground(Void... voids) {
        // Ask for records

        return Application.getDataHandler(activity).getApplications();
    }
    @Override
    protected void onPostExecute(List<Pojo> pojos) {
        super.onPostExecute(pojos);
        activity.adapter2.clear();

        Collection<Result> results = new ArrayList<>();

        if (pojos != null) {
            for (int i = pojos.size() - 1; i >= 0; i--) {


                results.add(Result.fromPojo(activity, pojos.get(i)));
            }

            activity.adapter2.addAll(results);
        }
        activity.resetTask();
    }
}
