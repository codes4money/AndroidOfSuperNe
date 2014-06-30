package com.donal.superne.app;

import android.app.Application;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by donal on 14-6-30.
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.allowD = true;
    }

}
