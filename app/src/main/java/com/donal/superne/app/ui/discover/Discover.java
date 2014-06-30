package com.donal.superne.app.ui.discover;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;

public class Discover extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
    }

    @Override
    protected void initNavgation() {
        setNavTitle(R.string.discover);
    }
}
