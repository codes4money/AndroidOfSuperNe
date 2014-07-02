package com.donal.superne.app.ui.register;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.config.AppManager;
import com.donal.superne.app.utils.StringUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class RegisterPass extends BaseActivity implements View.OnClickListener{

    @ViewInject(R.id.imgAvatar)
    private ImageView imgAvatar;

    @ViewInject(R.id.edtNickname)
    private EditText edtNickname;

    @ViewInject(R.id.edtPassword)
    private EditText edtPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pass);
        initNavgation();
    }

    @Override
    protected void initNavgation() {
        super.initNavgation();
        setNavTitle(R.string.register);
        setBtnLeft(this);
        setBtnRight(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLeft:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnRight:
                submitInfo();
                break;
        }
    }

    private void submitInfo() {
        String nickname = edtNickname.getEditableText().toString();
        String password = edtPassword.getEditableText().toString();
        if (StringUtils.empty(nickname)) {
            return;
        }
        if (StringUtils.doEmpty(password).length() < 6) {
            return;
        }
    }
}
