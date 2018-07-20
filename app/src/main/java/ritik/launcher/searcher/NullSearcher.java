package ritik.launcher.searcher;

import java.util.ArrayList;
import java.util.List;

import ritik.launcher.MainActivity;
import ritik.launcher.pojo.Pojo;

/**
 * Retrieve pojos from history
 */
public class NullSearcher extends Searcher {

    public NullSearcher(MainActivity activity) {
        super(activity);
    }

    @Override
    protected List<Pojo> doInBackground(Void... voids) {
        return new ArrayList<>();
    }
}
