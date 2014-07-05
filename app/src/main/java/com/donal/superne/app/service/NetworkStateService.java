package com.donal.superne.app.service;

import android.app.Service;
import android.content.*;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;

import com.donal.superne.app.config.Constant;

public class NetworkStateService extends Service
{


    private ConnectivityManager connectivityManager;
    private NetworkInfo info;

    private SharedPreferences shared;
    private SharedPreferences.Editor editor;

    private BroadcastReceiver mReceiver = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION))
            {
                connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                info = connectivityManager.getActiveNetworkInfo();
                if (info != null && info.isAvailable())
                {
                    String name = info.getTypeName();
                    if (name.equals("WIFI"))
                    { // Wifi
                        editor.putString("netType", "wifi");
                        editor.commit();
                        sendBroadcast(new Intent().setAction(ConnectivityManager.CONNECTIVITY_ACTION).putExtra(Constant.CONNECT_TYPE, Constant.WIFI));
                    }
                    else
                    { // 2G/3G
                        editor.putString("netType", "3g");
                        editor.commit();
                        sendBroadcast(new Intent().setAction(ConnectivityManager.CONNECTIVITY_ACTION).putExtra(Constant.CONNECT_TYPE, Constant.GPRS));
                    }
                }
                else
                { // 无网络
                    editor.putString("netType", "null");
                    editor.commit();
                    sendBroadcast(new Intent().setAction(ConnectivityManager.CONNECTIVITY_ACTION).putExtra(Constant.CONNECT_TYPE, Constant.NO_CONNECT));
                }

            }
        }
    };

    @Override
    public IBinder onBind(Intent intent)
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate()
    {
        super.onCreate();

        shared = getSharedPreferences("userInfo", 0);
        editor = shared.edit();

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(mReceiver, mFilter);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(mReceiver);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        return super.onStartCommand(intent, flags, startId);
    }

}
