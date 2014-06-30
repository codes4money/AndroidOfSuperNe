package com.donal.superne.app;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * Created by donal on 14-6-30.
 */
public abstract class BaseActivity extends Activity {
    @ViewInject(R.id.btnLeft)
    private Button btnLeft;

    @ViewInject(R.id.tvTitle)
    private TextView tvTitle;

    @ViewInject(R.id.btnRight)
    private Button  btnRight;

    protected void setNavTitle(int resource) {
        setNavTitle(getResources().getString(resource));
    }

    protected void setNavTitle(String title) {
        LogUtils.d(title);
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

    protected abstract void initNavgation();


}
