package com.donal.superne.app.ui.profile;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.config.AppManager;
import com.donal.superne.app.manager.XmppConnectionManager;
import com.donal.superne.app.ui.Welcome;
import com.donal.superne.app.ui.login.Login;
import org.apache.http.client.CookieStore;

public class Profile extends BaseActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        initNavgation();
    }

    @Override
    protected void initNavgation() {
        super.initNavgation();
        setNavTitle(R.string.profile);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnAlbum:
                break;
            case R.id.btnWeibo:
                break;
            case R.id.btnLocation:
                break;
            case R.id.btnModifyPassword:
                break;
            case R.id.btnNotification:
                break;
            case R.id.btnLogout:
                logout();
                break;
        }
    }

    private void logout() {
        new AlertDialog.Builder(this).setTitle(R.string.logout)
                .setNeutralButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        XmppConnectionManager.getInstance().disconnect();
                        stopService();
                        AppManager.getAppManager().finishAllActivity();
                        startActivity(new Intent(Profile.this, Welcome.class));
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
}
