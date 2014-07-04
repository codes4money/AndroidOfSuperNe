package com.donal.superne.app.ui.contact;

import android.database.Cursor;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ListView;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.BaseApplication;
import com.donal.superne.app.R;
import com.donal.superne.app.config.Constant;
import com.donal.superne.app.manager.DatabaseManager;
import com.donal.superne.app.manager.XmppConnectionManager;
import com.donal.superne.app.ui.contact.adapter.ContactAdapter;
import com.donal.superne.app.widget.MyLetterListView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.view.annotation.ViewInject;
import org.jivesoftware.smackx.superne.User;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
        getFriends();
    }

    @Override
    protected void initNavgation() {
        super.initNavgation();
        setNavTitle(R.string.contact);
        View header = LayoutInflater.from(this).inflate(R.layout.header_contact, null);
        lvContact.addHeaderView(header);
        try {
            Selector selector = Selector.from(User.class).orderBy("username");
            Cursor cursor = DatabaseManager.getInstance().getDB().execQuery(selector.toString());
            cursor.setNotificationUri(context.getContentResolver(), Constant.USER_URI);
            contactAdapter = new ContactAdapter(this, cursor, true);
            lvContact.setAdapter(contactAdapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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

    private void getFriends() {
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        BaseApplication.getInstance().getContentResolver().notifyChange(Constant.USER_URI, null);
                        break;
                }
            }
        };
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final Message msg = new Message();
                try {
                    XmppConnectionManager.getInstance().getFriends(new XmppConnectionManager.XMPPCallback() {
                        @Override
                        public void onSuccess(Object object) {
                            try {
                                List<User> users = (List<User>) object;
                                DatabaseManager.getInstance().getDB().saveOrUpdateAll(users);
                                msg.what = 1;
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                                msg.what = -1;
                            }
                            handler.sendMessage(msg);
                        }

                        @Override
                        public void onFailure(Object object) {
                            msg.obj = object;
                            msg.what = 0;
                            handler.sendMessage(msg);
                        }
                    });
                }
                catch (Exception e) {
                    msg.what = -1;
                    handler.sendMessage(msg);
                }
            }
        });
    }
}
