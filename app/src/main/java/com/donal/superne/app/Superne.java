package com.donal.superne.app;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;

import com.donal.superne.app.client.Preferences;
import com.donal.superne.app.config.AppConfig;
import com.donal.superne.app.config.AppException;
import com.donal.superne.app.config.AppManager;
import com.donal.superne.app.config.MethodsCompat;
import com.donal.superne.app.service.MessageCenterService;
import com.donal.superne.app.utils.StringUtils;
import com.lidroid.xutils.util.LogUtils;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import org.jivesoftware.smackx.superne.Registration;

import java.io.*;
import java.util.Hashtable;
import java.util.Properties;

/**
 * Created by donal on 14-6-30.
 */
public class Superne extends Application
{
    private static final int                       CACHE_TIME     = 60 * 60000;//缓存失效时间
    private              Hashtable<String, Object> memCacheRegion = new Hashtable<String, Object>();
    private static Superne superne;
    private        boolean isLogin;


    private SharedPreferences.OnSharedPreferenceChangeListener mPrefChangedListener;

    @Override
    public void onCreate()
    {
        super.onCreate();
        LogUtils.allowD = BuildConfig.DEBUG;
        superne = this;
        initAUIL(this);
        Thread.setDefaultUncaughtExceptionHandler(AppException.getAppExceptionHandler());

        Preferences.init(this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mPrefChangedListener = new SharedPreferences.OnSharedPreferenceChangeListener()
        {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences prefs, String key)
            {

            }
        };
        prefs.registerOnSharedPreferenceChangeListener(mPrefChangedListener);

        setServicesEnabled(this, true);

    }

    public synchronized static Superne getInstance()
    {
        return superne;
    }

    /**
     * 初始化AUIL
     *
     * @param context
     */
    private void initAUIL(Context context)
    {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        ImageLoader.getInstance().init(config);
    }

    /**
     * 设置登录状态
     *
     * @param status
     */
    public void setIsLogin(final boolean status)
    {
        try
        {
            this.isLogin = status;
            setProperties(new Properties()
            {
                {
                    setProperty("user.login", status ? "1" : "0");
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * 是否登录
     *
     * @return
     */
    public boolean returnIsLogin()
    {
        try
        {
            String loginStr = getProperty("user.login");
            if (StringUtils.empty(loginStr))
            {
                isLogin = false;
            }
            else
            {
                isLogin = (loginStr.equals("1")) ? true : false;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return isLogin;
    }

    /**
     * 登录账号信息
     *
     * @param registeration
     */
    public void setRegisterationInfo(final Registration registeration)
    {
        setProperties(new Properties()
        {
            {
                setProperty("registeration.name", registeration.getUsername());
                setProperty("registeration.avatar", registeration.getAvatar());
                setProperty("registeration.nickname", registeration.getName());
                setProperty("registeration.password", registeration.getPassword());
            }
        });
    }

    /**
     * @return
     */
    public Registration getRegisterationInfo()
    {
        Registration registration = new Registration();
        registration.setUsername(getProperty("registeration.name"));
        registration.setAvatar(getProperty("registeration.avatar"));
        registration.setName(getProperty("registeration.nickname"));
        registration.setPassword(getProperty("registeration.password"));
        return registration;
    }

    /**
     * exit
     */
    public void exit()
    {
        AppManager.getAppManager().finishAllActivity();
    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode)
    {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    /**
     * 获取App安装包信息
     *
     * @return
     */
    public PackageInfo getPackageInfo()
    {
        PackageInfo info = null;
        try
        {
            info = getPackageManager().getPackageInfo(getPackageName(), 0);
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace(System.err);
        }
        if (info == null) info = new PackageInfo();
        return info;
    }

    /**
     * 判断缓存数据是否可读
     *
     * @param cachefile
     * @return true可读
     */
    private boolean isReadDataCache(String cachefile)
    {
        return readObject(cachefile) != null;
    }

    /**
     * 判断缓存是否存在
     *
     * @param cachefile
     * @return
     */
    private boolean isExistDataCache(String cachefile)
    {
        boolean exist = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists())
            exist = true;
        return exist;
    }

    /**
     * 判断缓存是否失效
     *
     * @param cachefile
     * @return
     */
    public boolean isCacheDataFailure(String cachefile)
    {
        boolean failure = false;
        File data = getFileStreamPath(cachefile);
        if (data.exists() && (System.currentTimeMillis() - data.lastModified()) > CACHE_TIME)
            failure = true;
        else if (!data.exists())
            failure = true;
        return failure;
    }

    /**
     * 清除app缓存
     */
    public void clearAppCache()
    {
        //清除webview缓存
//		File file = CacheManager.
//		if (file != null && file.exists() && file.isDirectory()) {
//		    for (File item : file.listFiles()) {
//		    	item.delete();
//		    }
//		    file.delete();
//		}
        deleteDatabase("webview.db");
        deleteDatabase("webview.db-shm");
        deleteDatabase("webview.db-wal");
        deleteDatabase("webviewCache.db");
        deleteDatabase("webviewCache.db-shm");
        deleteDatabase("webviewCache.db-wal");
        //清除数据缓存
        clearCacheFolder(getFilesDir(), System.currentTimeMillis());
        clearCacheFolder(getCacheDir(), System.currentTimeMillis());
        //2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO))
        {
            clearCacheFolder(MethodsCompat.getExternalCacheDir(this), System.currentTimeMillis());
        }
        //清除编辑器保存的临时内容
        Properties props = getProperties();
        for (Object key : props.keySet())
        {
            String _key = key.toString();
            if (_key.startsWith("temp"))
                removeProperty(_key);
        }
    }

    /**
     * 清除缓存目录
     *
     * @param dir     目录
     * @param curTime 当前系统时间
     * @return
     */
    private int clearCacheFolder(File dir, long curTime)
    {
        int deletedFiles = 0;
        if (dir != null && dir.isDirectory())
        {
            try
            {
                for (File child : dir.listFiles())
                {
                    if (child.isDirectory())
                    {
                        deletedFiles += clearCacheFolder(child, curTime);
                    }
                    if (child.lastModified() < curTime)
                    {
                        if (child.delete())
                        {
                            deletedFiles++;
                        }
                    }
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return deletedFiles;
    }

    /**
     * 将对象保存到内存缓存中
     *
     * @param key
     * @param value
     */
    public void setMemCache(String key, Object value)
    {
        memCacheRegion.put(key, value);
    }

    /**
     * 从内存缓存中获取对象
     *
     * @param key
     * @return
     */
    public Object getMemCache(String key)
    {
        return memCacheRegion.get(key);
    }

    /**
     * 保存磁盘缓存
     *
     * @param key
     * @param value
     * @throws java.io.IOException
     */
    public void setDiskCache(String key, String value) throws IOException
    {
        FileOutputStream fos = null;
        try
        {
            fos = openFileOutput("cache_" + key + ".data", Context.MODE_PRIVATE);
            fos.write(value.getBytes());
            fos.flush();
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (Exception e)
            {
            }
        }
    }

    /**
     * 获取磁盘缓存数据
     *
     * @param key
     * @return
     * @throws IOException
     */
    public String getDiskCache(String key) throws IOException
    {
        FileInputStream fis = null;
        try
        {
            fis = openFileInput("cache_" + key + ".data");
            byte[] datas = new byte[fis.available()];
            fis.read(datas);
            return new String(datas);
        }
        finally
        {
            try
            {
                fis.close();
            }
            catch (Exception e)
            {
            }
        }
    }

    /**
     * 保存对象
     *
     * @param ser
     * @param file
     * @throws IOException
     */
    public boolean saveObject(Serializable ser, String file)
    {
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try
        {
            fos = openFileOutput(file, MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(ser);
            oos.flush();
            return true;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        finally
        {
            try
            {
                oos.close();
            }
            catch (Exception e)
            {
            }
            try
            {
                fos.close();
            }
            catch (Exception e)
            {
            }
        }
    }

    /**
     * 读取对象
     *
     * @param file
     * @return
     * @throws IOException
     */
    public Serializable readObject(String file)
    {
        if (!isExistDataCache(file))
            return null;
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try
        {
            fis = openFileInput(file);
            ois = new ObjectInputStream(fis);
            return (Serializable) ois.readObject();
        }
        catch (FileNotFoundException e)
        {
        }
        catch (OutOfMemoryError e)
        {
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //反序列化失败 - 删除缓存文件
            if (e instanceof InvalidClassException)
            {
                File data = getFileStreamPath(file);
                data.delete();
            }
        }
        finally
        {
            try
            {
                ois.close();
            }
            catch (Exception e)
            {
            }
            try
            {
                fis.close();
            }
            catch (Exception e)
            {
            }
        }
        return null;
    }

    public boolean containsProperty(String key)
    {
        Properties props = getProperties();
        return props.containsKey(key);
    }

    public void setProperties(Properties ps)
    {
        AppConfig.getAppConfig(this).set(ps);
    }

    public Properties getProperties()
    {
        return AppConfig.getAppConfig(this).get();
    }

    public void setProperty(String key, String value)
    {
        AppConfig.getAppConfig(this).set(key, value);
    }

    public String getProperty(String key)
    {
        return AppConfig.getAppConfig(this).get(key);
    }

    public void removeProperty(String... key)
    {
        AppConfig.getAppConfig(this).remove(key);
    }

    /**
     * Enable/disable application components when account is added or removed.
     */
    public static void setServicesEnabled(Context context, boolean enabled)
    {
        PackageManager pm = context.getPackageManager();
        enableService(context, pm, MessageCenterService.class, enabled);
    }

    private static void enableService(Context context, PackageManager pm, Class<?> klass, boolean enabled)
    {
        pm.setComponentEnabledSetting(new ComponentName(context, klass),
                enabled ? PackageManager.COMPONENT_ENABLED_STATE_DEFAULT : PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }


}
