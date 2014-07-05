package com.donal.superne.app.ui.login;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.client.Preferences;
import com.donal.superne.app.config.AppManager;
import com.donal.superne.app.config.Constant;
import com.donal.superne.app.service.MessageCenterService;
import com.donal.superne.app.ui.register.Register;
import com.donal.superne.app.ui.tabbar.Tabbar;
import com.donal.superne.app.utils.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;


public class Login extends BaseActivity
{

    @ViewInject(R.id.edtAcount)
    private EditText edtAcount;

    @ViewInject(R.id.edtPassword)
    private EditText edtPassword;

    @ViewInject(R.id.btnLogin)
    private Button btnLogin;

    @ViewInject(R.id.btnReg)
    private Button btnReg;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ViewUtils.inject(this);
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.ACTION_CONNECTED);
        registerReceiver(loginReceiver, filter);
        edtAcount.setText(Preferences.getPrefUserName());
        edtPassword.setText(Preferences.getPrefPassword());
    }

//    public void ButtonClick(View view)
//    {
//        switch (view.getId())
//        {
//            case R.id.btnLogin:
//                login();
//                break;
//            case R.id.btnReg:
//                register();
//                break;
//        }
//    }

    public void login(View view)
    {
        String account = edtAcount.getEditableText().toString();
        String password = edtPassword.getEditableText().toString();
        if (StringUtils.empty(account))
        {
            return;
        }
        if (StringUtils.empty(password))
        {
            return;
        }
//        if (!StringUtils.isMobileNO(account))
//        {
//            return;
//        }

        if (Preferences.saveAccount(account, password))
        {
            MessageCenterService.login(this);
        }

        loadingPd = showProgress(context, null, null, true);
//        final Registration registration = new Registration();
//        registration.setAvatar("");
//        registration.setUsername(account);
//        registration.setName("");
//        registration.setPassword(password);
//        final Handler handler = new Handler()
//        {
//            @Override
//            public void handleMessage(Message msg)
//            {
//                switch (msg.what)
//                {
//                    case 1:
//                        setResult(RESULT_OK);
//                        startActivity(new Intent(Login.this, Tabbar.class));
//                        AppManager.getAppManager().finishActivity(Login.this);
//                        break;
//                    case 0:
//                        LogUtils.d((String) msg.obj);
//                        showToast((String) msg.obj);
//                        break;
//                    case -1:
//                        break;
//                }
//                dismissProgress(loadingPd);
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
//                    superne.setRegisterationInfo(registration);
//                    XmppConnectionManager.getInstance().login(registration.getUsername(), registration.getPassword(), new XmppConnectionManager.XMPPCallback()
//                    {
//                        @Override
//                        public void onSuccess(Object object)
//                        {
//                            //mark is logined
//                            superne.setIsLogin(true);
//                            try
//                            {
//                                List<org.jivesoftware.smack.packet.Message> offMessages = new OffineManager(XmppConnectionManager.getInstance().getConnection()).getMessages();
//                                XmppConnectionManager.getInstance().setAvailable();
//                                msg.what = 1;
//                                msg.obj = offMessages;
//                                handler.sendMessage(msg);
//                            }
//                            catch (Exception e)
//                            {
//                                msg.what = -1;
//                                handler.sendMessage(msg);
//                            }
//                        }
//
//                        @Override
//                        public void onFailure(Object message)
//                        {
//                            msg.what = 0;
//                            msg.obj = message;
//                            handler.sendMessage(msg);
//                        }
//                    });
//                }
//                catch (Exception e)
//                {
//                    e.printStackTrace();
//                    msg.what = -1;
//                    handler.sendMessage(msg);
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        unregisterReceiver(loginReceiver);
    }

    private void register()
    {
        startActivityForResult(new Intent(this, Register.class), Constant.REQUEST_REGISTER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != RESULT_OK)
        {
            return;
        }
        switch (requestCode)
        {
            case Constant.REQUEST_REGISTER:
                startActivity(new Intent(this, Tabbar.class));
                AppManager.getAppManager().finishActivity(this);
                break;
        }
    }

    private BroadcastReceiver loginReceiver = new BroadcastReceiver()
    {
        @Override
        public void onReceive(Context context, Intent intent)
        {
            if (Constant.ACTION_CONNECTED.equals(intent.getAction()))
            {
                dismissProgress(loadingPd);
                startActivity(new Intent(Login.this, Tabbar.class));
                AppManager.getAppManager().finishActivity(Login.this);
            }
        }
    };
}
