package com.donal.superne.app.ui.search;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.config.AppManager;

public class PeopleNearBy extends BaseActivity implements View.OnClickListener
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_near_by);
        initNavgation();
    }

    @Override
    protected void initNavgation()
    {
        super.initNavgation();
        setNavTitle(R.string.people_nearby);
        setBtnLeft(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnLeft:
                AppManager.getAppManager().finishActivity(this);
                break;
        }
    }
}
