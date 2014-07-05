package com.donal.superne.app.ui.chat;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;

import org.jivesoftware.smackx.superne.User;

public class ChatActivity extends BaseActivity
{

    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        mUser = (User) getIntent().getSerializableExtra("user");

    }

    @Override
    protected void initNavgation()
    {
        super.initNavgation();
        if (mUser!=null)
            setNavTitle(mUser.getName());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.chat, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings)
        {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
