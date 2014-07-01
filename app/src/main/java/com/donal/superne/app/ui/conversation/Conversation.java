package com.donal.superne.app.ui.conversation;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.manager.OffineManager;
import com.donal.superne.app.manager.XmppConnectionManager;
import com.lidroid.xutils.util.LogUtils;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.List;


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
                        LogUtils.d("aaaa");
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
                    XMPPTCPConnection xmpptcpConnection = XmppConnectionManager.getInstance().getConnection();
                    xmpptcpConnection.login("8001", "123", "Android");
//                    List<org.jivesoftware.smack.packet.Message> offMessages = new OffineManager(xmpptcpConnection).getMessages();
                    XmppConnectionManager.setAvailable();
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
