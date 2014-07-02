package com.donal.superne.app.ui.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
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


}
