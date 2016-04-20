package util;

import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by 58 on 2016/4/19.
 */
public class Mytask extends AsyncTask {

    private Context mcontext = null;
    private String pkg = null;

    public Mytask(Context tmp, String pkg){
        super();
        this.mcontext = tmp;
        this.pkg = pkg;
    }
    @Override
    protected Object doInBackground(Object[] objects) {

        return null;
    }
}
