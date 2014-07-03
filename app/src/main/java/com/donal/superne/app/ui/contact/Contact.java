package com.donal.superne.app.ui.contact;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.widget.MyLetterListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class Contact extends BaseActivity {

    @ViewInject(R.id.lvContact)
    private ListView lvContact;

    @ViewInject(R.id.leAlpha)
    private MyLetterListView leAlpha;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ViewUtils.inject(this);
        initNavgation();
    }

    @Override
    protected void initNavgation() {
        super.initNavgation();
        setNavTitle(R.string.contact);
    }
}
