package com.donal.superne.app.ui.conversation;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.config.Constant;
import com.donal.superne.app.manager.OffineManager;
import com.donal.superne.app.manager.XmppConnectionManager;
import com.donal.superne.app.model.register.Registration;
import com.lidroid.xutils.util.LogUtils;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Conversation extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        IntentFilter filter =  new IntentFilter();
        filter.addAction(Constant.CONNECT_ACTION);
        registerReceiver(receiver, filter);
        initNavgation();
        connect2xmpp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        isExit();
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
                        startService();
                        break;
                    case 0:
                        LogUtils.d((String)msg.obj);
                        break;
                    case -1:
                        break;
                }
            }
        };
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final Message msg = new Message();
                try
                {
                    Registration registration = baseApplication.getRegisterationInfo();
                    XmppConnectionManager.getInstance().login(registration.getUsername(), registration.getPassword(), new XmppConnectionManager.XMPPCallback() {
                        @Override
                        public void onSuccess() {
                            //mark is logined
                            baseApplication.setIsLogin(true);
                            try
                            {
                                List<org.jivesoftware.smack.packet.Message> offMessages = new OffineManager(XmppConnectionManager.getInstance().getConnection()).getMessages();
                                XmppConnectionManager.getInstance().setAvailable();
                                msg.what = 1;
                                msg.obj = offMessages;
                                handler.sendMessage(msg);
                            }
                            catch (Exception e)
                            {
                                msg.what = -1;
                                handler.sendMessage(msg);
                            }
                        }

                        @Override
                        public void onFailure(Object message) {
                            msg.what = 0;
                            msg.obj = message;
                            handler.sendMessage(msg);
                        }
                    });
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                    msg.what = -1;
                    handler.sendMessage(msg);
                }

            }
        });
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (Constant.CONNECT_ACTION.equals(action)) {
                int connectType = intent.getIntExtra(Constant.CONNECT_TYPE, 0);
                switch (connectType) {
                    case Constant.WIFI:
                    case Constant.GPRS:
                        break;
                    case Constant.NO_CONNECT:
                        break;
                }
            }
        }

    };
}
