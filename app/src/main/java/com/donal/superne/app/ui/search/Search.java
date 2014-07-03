package com.donal.superne.app.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.config.AppManager;

public class Search extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        initNavgation();
    }

    @Override
    protected void initNavgation() {
        super.initNavgation();
        setNavTitle(R.string.search);
        setBtnLeft(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLeft:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnNearBy:
                startActivity(new Intent(this, PeopleNearBy.class));
                break;
            case R.id.btnSearchByPhone:
                startActivity(new Intent(this, SearchByPhone.class));
                break;
        }
    }
}
