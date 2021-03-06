package com.donal.superne.app.ui.discover;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.ui.search.PeopleNearBy;
import com.donal.superne.app.ui.search.Search;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class Discover extends BaseActivity implements View.OnClickListener
{

    @ViewInject(R.id.btnMoments)
    private RelativeLayout btnMoments;

    @ViewInject(R.id.btnNearBy)
    private RelativeLayout btnNearBy;

    @ViewInject(R.id.btnScan)
    private RelativeLayout btnScan;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover);
        ViewUtils.inject(this);
        initNavgation();

    }

    @Override
    protected void initNavgation()
    {
        super.initNavgation();
        setNavTitle(R.string.discover);
        btnMoments.setOnClickListener(this);
        btnNearBy.setOnClickListener(this);
        btnScan.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnMoments:
                startActivity(new Intent(this, Search.class));
                break;
            case R.id.btnNearBy:
                startActivity(new Intent(this, PeopleNearBy.class));
                break;
            case R.id.btnScan:
                startActivity(new Intent(this, CaptureActivity.class));
                break;
        }
    }
}
