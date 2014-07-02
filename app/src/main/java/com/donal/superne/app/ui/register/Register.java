package com.donal.superne.app.ui.register;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.config.Constant;
import com.donal.superne.app.utils.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class Register extends BaseActivity implements View.OnClickListener{

    @ViewInject(R.id.edtAccount)
    private EditText edtAccount;

    @ViewInject(R.id.btnReg)
    private Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
        initNavgation();
    }

    @Override
    protected void initNavgation() {
        super.initNavgation();
        setBtnLeft(this);
        setBtnRight(this);
        btnReg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnReg:
                register();
                break;
        }
    }

    private void register() {
        String account = edtAccount.getEditableText().toString();
        if (StringUtils.empty(account)) {
            return;
        }
        if (!StringUtils.isMobileNO(account)) {
            return;
        }
        startActivityForResult(new Intent(this, RegisterPass.class).putExtra(Constant.MOBILE, account), Constant.REQUEST_REGISTERPASS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUEST_REGISTERPASS:
                break;
        }
    }
}
