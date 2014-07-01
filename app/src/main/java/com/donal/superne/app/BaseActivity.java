package com.donal.superne.app;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.donal.superne.app.config.AppManager;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import org.w3c.dom.Text;

/**
 * Created by donal on 14-6-30.
 */
public abstract class BaseActivity extends Activity {
    private Button btnLeft;
    private TextView tvTitle;
    private Button  btnRight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getAppManager().addActivity(this);
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


}
