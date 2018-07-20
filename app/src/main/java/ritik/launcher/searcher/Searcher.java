package ritik.launcher.searcher;


import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ritik.launcher.MainActivity;
import ritik.launcher.pojo.Pojo;
import ritik.launcher.result.Result;

public abstract class Searcher extends AsyncTask<Void, Void, List<Pojo>> {
    protected static final int DEFAULT_MAX_RESULTS = 25;

    final MainActivity activity;

    Searcher(MainActivity activity) {
        super();
        this.activity = activity;
    }


    @Override
    protected void onPostExecute(List<Pojo> pojos) {
        super.onPostExecute(pojos);
        activity.adapter.clear();

        Collection<Result> results = new ArrayList<>();

        if (pojos != null) {
            for (int i = pojos.size() - 1; i >= 0; i--) {
                results.add(Result.fromPojo(activity, pojos.get(i)));
            }

            activity.adapter.addAll(results);
        }
        activity.resetTask();
    }
}
