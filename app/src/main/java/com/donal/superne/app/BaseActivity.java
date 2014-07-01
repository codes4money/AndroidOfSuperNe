package com.donal.superne.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.donal.superne.app.config.AppActivitySupport;
import com.donal.superne.app.config.AppManager;
import com.donal.superne.app.utils.StringUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import org.w3c.dom.Text;

/**
 * Created by donal on 14-6-30.
 */
public abstract class BaseActivity extends Activity implements AppActivitySupport {
    private Button btnLeft;
    private TextView tvTitle;
    private Button  btnRight;

    protected BaseApplication baseApplication;
    protected Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
        baseApplication = (BaseApplication) getApplication();
        context = this;
    }

    protected void setNavTitle(int resource) {
        tvTitle.setText(resource);
    }

    protected void setNavTitle(String title) {
        tvTitle.setText(title);
    }

    protected void setBtnLeft(View.OnClickListener listener) {
        btnLeft.setVisibility(View.VISIBLE);
        btnLeft.setOnClickListener(listener);
    }

    protected void setBtnRight(View.OnClickListener listener) {
        btnRight.setVisibility(View.VISIBLE);
        btnRight.setOnClickListener(listener);
    }

    protected void initNavgation() {
        btnLeft = (Button) findViewById(R.id.btnLeft);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        btnRight = (Button) findViewById(R.id.btnRight);
    }

    @Override
    public BaseApplication getBaseApplication() {
        return baseApplication;
    }

    @Override
    public void stopService() {

    }

    @Override
    public void startService() {

    }

    @Override
    public boolean validateInternet() {
        return false;
    }

    @Override
    public boolean hasInternetConnected() {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo network = manager.getActiveNetworkInfo();
            if (network != null && network.isConnectedOrConnecting()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void openWirelessSet() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder
                .setTitle(R.string.prompt)
                .setMessage(context.getString(R.string.check_connection))
                .setPositiveButton(R.string.menu_settings,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                Intent intent = new Intent(
                                        Settings.ACTION_WIRELESS_SETTINGS);
                                context.startActivity(intent);
                            }
                        })
                .setNegativeButton(R.string.close,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                dialog.cancel();
                            }
                        });
        dialogBuilder.show();
    }

    @Override
    public void isExit() {

    }

    @Override
    public boolean hasLocationGPS() {
        return false;
    }

    @Override
    public boolean hasLocationNetWork() {
        return false;
    }

    @Override
    public void checkMemoryCard() {

    }

    @Override
    public void showToast(String text, int longint) {

    }

    @Override
    public void showToast(String text) {

    }

    @Override
    public void showDialog(String message) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        }
        catch (Exception e) {

        }
    }

    @Override
    public void showDialog(String message, String positive, String negative, final DialogClickListener dialogClickListener) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(message);
            builder.setPositiveButton(positive, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    dialogClickListener.ok();
                }
            });
            if (StringUtils.notEmpty(negative)) {
                builder.setNegativeButton(negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int arg1) {
                        dialog.dismiss();
                        dialogClickListener.cancel();
                    }
                });
            }
            builder.create().show();
        }
        catch (Exception e) {
        }
    }

    @Override
    public ProgressDialog getProgressDialog() {
        return null;
    }

    @Override
    public Context getContext() {
        return context;
    }

    @Override
    public SharedPreferences getLoginUserSharedPre() {
        return null;
    }

    @Override
    public void setNotiType(int iconId, String contentTitle, String contentText, Class activity, String from) {

    }
}
