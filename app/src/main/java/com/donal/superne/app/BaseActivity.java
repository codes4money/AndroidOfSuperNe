package com.donal.superne.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.donal.superne.app.config.AppActivitySupport;
import com.donal.superne.app.config.AppManager;
import com.donal.superne.app.manager.XmppConnectionManager;
import com.donal.superne.app.service.ChatReceiveService;
import com.donal.superne.app.service.NetworkStateService;
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

    protected ProgressDialog loadingPd;

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

    protected Button getBtnRight() {
        return btnRight;
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
        stopService(new Intent(context, ChatReceiveService.class));
    }

    @Override
    public void startService() {
        startService(new Intent(context, ChatReceiveService.class));
    }

    @Override
    public boolean validateInternet() {
        ConnectivityManager manager = (ConnectivityManager) context
                .getSystemService(context.CONNECTIVITY_SERVICE);
        if (manager == null) {
            openWirelessSet();
            return false;
        } else {
            NetworkInfo[] info = manager.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        openWirelessSet();
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
        new AlertDialog.Builder(context).setTitle(R.string.exit)
                .setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        XmppConnectionManager.getInstance().disconnect();
                        baseApplication.exit();
                        stopService();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).show();
    }

    @Override
    public boolean hasLocationGPS() {
        LocationManager manager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        if (manager
                .isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean hasLocationNetWork() {
        LocationManager manager = (LocationManager) context
                .getSystemService(context.LOCATION_SERVICE);
        if (manager
                .isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void checkMemoryCard() {
        if (!Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            new AlertDialog.Builder(context)
                    .setTitle(R.string.prompt)
                    .setMessage(R.string.check_sdcard)
                    .setPositiveButton(R.string.menu_settings,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    Intent intent = new Intent(
                                            Settings.ACTION_SETTINGS);
                                    context.startActivity(intent);
                                }
                            })
                    .setNegativeButton(R.string.exit,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    baseApplication.exit();
                                }
                            }).create().show();
        }
    }

    @Override
    public void showToast(String text, int longint) {
        Toast.makeText(context, text, longint).show();
    }

    @Override
    public void showToast(String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
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

    public void closeInput() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null && this.getCurrentFocus() != null) {
            inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 显示进度条
     *
     * @param context
     *            环境
     * @param title
     *            标题
     * @param message
     *            信息
     * @return
     */
    public static ProgressDialog showProgress(final Context context,
                                              CharSequence title, CharSequence message) {
        try {
            if (context == null) {
                return null;
            }
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setCancelable(false);
            dialog.show();
            return dialog;
        } catch (Exception e) {
            return null;
        }
    }

    public static ProgressDialog showProgress(Context context,
                                              CharSequence title, CharSequence message,
                                              boolean cancelable) {
        try {
            if (context == null) {
                return null;
            }
            final ProgressDialog dialog = new ProgressDialog(context);
            dialog.setTitle(title);
            dialog.setMessage(message);
            dialog.setCancelable(cancelable);
            new CountDownTimer(10000,1000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }
                public void onFinish() {
                    try {
                        dialog.dismiss();
                    } catch (Exception e) {

                    }
                }
            }.start();
            dialog.show();
            return dialog;
        } catch (Exception e) {
            return null;
        }
    }

    public static ProgressDialog dismissProgress(ProgressDialog progressDialog) {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return progressDialog;
    }
}
