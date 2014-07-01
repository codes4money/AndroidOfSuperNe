package com.donal.superne.app;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.donal.superne.app.config.AppException;
import com.lidroid.xutils.util.LogUtils;

/**
 * Created by donal on 14-6-30.
 */
public class BaseApplication extends Application {

    private static BaseApplication baseApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        LogUtils.allowD = true;
        baseApplication = this;
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());
    }

    public synchronized static BaseApplication getInstance() {
        return baseApplication;
    }

    /**
     * 获取App安装包信息
     * @return
     */
    public PackageInfo getPackageInfo() {
        PackageInfo info = null;
        try {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace(System.err);
        }
        if(info == null) info = new PackageInfo();
        return info;
    }

}
