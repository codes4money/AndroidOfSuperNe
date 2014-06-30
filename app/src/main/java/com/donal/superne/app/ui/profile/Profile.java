package com.donal.superne.app.ui.profile;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;

public class Profile extends BaseActivity {

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

    public void ButtonClick(View v) {
        switch (v.getId()) {
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
                break;
        }
    }
}
