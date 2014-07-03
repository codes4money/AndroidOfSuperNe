package com.donal.superne.app.ui.search;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.config.AppManager;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SearchByPhone extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.edtPhone)
    private EditText edtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_phone);
        ViewUtils.inject(this);
        initNavgation();
    }

    @Override
    protected void initNavgation() {
        super.initNavgation();
        setNavTitle(R.string.search_by_phone);
        setBtnLeft(this);
        setBtnRight(this);
        getBtnRight().setText(R.string.search);
        getBtnRight().setTextColor(getResources().getColor(R.color.white));
        getBtnRight().setTextSize(15);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLeft:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnRight:
                break;
        }
    }
}
