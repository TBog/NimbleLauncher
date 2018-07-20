package ritik.launcher.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;

import ritik.launcher.R;

/**
 * Created by SuperUser on 28-10-2017.
 */

public class ViewPageradapter extends PagerAdapter

{
    public Object instantiateItem(View collection, int position) {

        int resId = 0;
        switch (position) {
            case 0:

                resId = android.R.id.list;
                break;
            case 1:
                resId = R.id.list2;
                break;

        }
        return collection.findViewById(resId);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }



}

