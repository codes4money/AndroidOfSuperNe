package com.donal.superne.app.ui.contact;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.config.Constant;
import com.donal.superne.app.manager.DBManager;
import com.donal.superne.app.ui.chat.ChatActivity;
import com.donal.superne.app.ui.contact.adapter.ContactAdapter;
import com.donal.superne.app.widget.MyLetterListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.jivesoftware.smackx.superne.User;

import java.util.ArrayList;
import java.util.List;

public class Contact extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{

    @ViewInject(R.id.lvContact)
    private ListView lvContact;

    @ViewInject(R.id.leAlpha)
    private MyLetterListView leAlpha;

    private List<User> contacts = new ArrayList<User>();
    private ContactAdapter contactAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ViewUtils.inject(this);
        initNavgation();
        getFriends();
    }

    @Override
    protected void initNavgation()
    {
        super.initNavgation();
        setNavTitle(R.string.contact);
        View header = LayoutInflater.from(this).inflate(R.layout.header_contact, null);
        lvContact.addHeaderView(header);
        try
        {
            Selector selector = Selector.from(User.class).orderBy("_id");
            Cursor cursor = DBManager.getDb().execQuery(selector.toString());
            cursor.setNotificationUri(context.getContentResolver(), Constant.USER_URI);
            contactAdapter = new ContactAdapter(this, cursor, true);
            lvContact.setAdapter(contactAdapter);
            lvContact.setOnItemClickListener(this);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnRecommend:
                break;
            case R.id.btnGroups:
                break;
        }
    }

    private void getFriends()
    {
//        final Handler handler = new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg)
//            {
//                switch (msg.what)
//                {
//                    case 1:
//                        Superne.getInstance().getContentResolver().notifyChange(Constant.USER_URI, null);
//                        break;
//                }
//            }
//        };
//        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
//        singleThreadExecutor.execute(new Runnable()
//        {
//            @Override
//            public void run()
//            {
//                final Message msg = new Message();
//                try
//                {
//                    XmppConnectionManager.getInstance().getFriends(new XmppConnectionManager.XMPPCallback()
//                    {
//                        @Override
//                        public void onSuccess(Object object)
//                        {
//                            try
//                            {
//                                List<User> users = (List<User>) object;
//
//                                msg.what = 1;
//                            }
//                            catch (Exception e)
//                            {
//                                e.printStackTrace();
//                                msg.what = -1;
//                            }
//                            handler.sendMessage(msg);
//                        }
//
//                        @Override
//                        public void onFailure(Object object)
//                        {
//                            msg.obj = object;
//                            msg.what = 0;
//                            handler.sendMessage(msg);
//                        }
//                    });
//                }
//                catch (Exception e)
//                {
//                    msg.what = -1;
//                    handler.sendMessage(msg);
//                }
//            }
//        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        User user = ((ContactCell) view).getUser();
        LogUtils.e(user.getName());
        Intent i = new Intent(this, ChatActivity.class);
        i.putExtra("user", user);
        startActivity(i);
    }
}
