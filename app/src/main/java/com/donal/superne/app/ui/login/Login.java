package com.donal.superne.app.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.config.Constant;
import com.donal.superne.app.ui.register.Register;
import com.donal.superne.app.utils.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


public class Login extends BaseActivity {

    @ViewInject(R.id.edtAcount)
    private EditText edtAcount;

    @ViewInject(R.id.edtPassword)
    private EditText edtPassword;

    @ViewInject(R.id.btnLogin)
    private Button btnLogin;

    @ViewInject(R.id.btnReg)
    private Button btnReg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
    }

    public void ButtonClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                login();
                break;
            case R.id.btnReg:
                register();
                break;
        }
    }

    private void login() {
        String account = edtAcount.getEditableText().toString();
        String password = edtPassword.getEditableText().toString();
        if (StringUtils.empty(account)) {
            return;
        }
        if (StringUtils.empty(password)) {
            return;
        }
        if (!StringUtils.isMobileNO(account)) {
            return;
        }
        if (password.length() < 6) {
            return;
        }

    }

    private void register() {
        startActivityForResult(new Intent(this, Register.class), Constant.REQUEST_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case Constant.REQUEST_REGISTER:
                break;
        }

    }
}
