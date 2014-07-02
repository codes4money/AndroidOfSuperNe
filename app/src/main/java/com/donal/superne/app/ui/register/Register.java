package com.donal.superne.app.ui.register;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class Register extends BaseActivity {

    @ViewInject(R.id.edtAcount)
    private EditText edtAcount;

    @ViewInject()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ViewUtils.inject(this);
    }
}
