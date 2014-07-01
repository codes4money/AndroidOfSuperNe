package com.donal.superne.app.ui.conversation;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.manager.XmppConnectionManager;


public class Conversation extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        initNavgation();
        connect2xmpp();
    }

    @Override
    protected void initNavgation() {
        super.initNavgation();
        setNavTitle(R.string.conversation);
    }

    private void connect2xmpp() {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        break;
                    case -1:
                        break;
                }
            }
        };
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message msg = new Message();
                try {

                    XmppConnectionManager.getInstance().getConnection();
                    msg.what = 1;
                }
                catch (Exception e) {
                    e.printStackTrace();
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        }).start();
    }
}
