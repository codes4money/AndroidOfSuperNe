package com.donal.superne.app.ui.contact;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.ui.contact.adapter.ContactAdapter;
import com.donal.superne.app.widget.MyLetterListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import org.jivesoftware.smackx.superne.User;

import java.util.ArrayList;
import java.util.List;

public class Contact extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.lvContact)
    private ListView lvContact;

    @ViewInject(R.id.leAlpha)
    private MyLetterListView leAlpha;

    private List<User> contacts = new ArrayList<User>();
    private ContactAdapter contactAdapter;
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
        View header = LayoutInflater.from(this).inflate(R.layout.header_contact, null);
        contactAdapter = new ContactAdapter(this, contacts);
        lvContact.addHeaderView(header);
        lvContact.setAdapter(contactAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRecommend:
                break;
            case R.id.btnGroups:
                break;
        }
    }
}
