package com.donal.superne.app.ui.register;

import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.config.AppManager;
import com.donal.superne.app.config.Constant;
import com.donal.superne.app.manager.XmppConnectionManager;
import com.donal.superne.app.utils.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import org.jivesoftware.smackx.superne.Registration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RegisterPass extends BaseActivity implements View.OnClickListener{

    @ViewInject(R.id.imgAvatar)
    private ImageView imgAvatar;

    @ViewInject(R.id.edtNickname)
    private EditText edtNickname;

    @ViewInject(R.id.edtPassword)
    private EditText edtPassword;

    private String mobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_pass);
        ViewUtils.inject(this);
        mobile = getIntent().getStringExtra(Constant.MOBILE);
        initNavgation();
    }

    @Override
    protected void initNavgation() {
        super.initNavgation();
        setNavTitle(R.string.register);
        setBtnLeft(this);
        setBtnRight(this);
        getBtnRight().setText(R.string.register);
        getBtnRight().setTextColor(getResources().getColor(R.color.white));
        getBtnRight().setTextSize(15);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLeft:
                AppManager.getAppManager().finishActivity(this);
                break;
            case R.id.btnRight:
                submitInfo();
                break;
        }
    }

    private void submitInfo() {
        String nickname = edtNickname.getEditableText().toString();
        String password = edtPassword.getEditableText().toString();
        if (StringUtils.empty(nickname)) {
            return;
        }
        if (StringUtils.doEmpty(password).length() < 6) {
            return;
        }
        loadingPd = showProgress(context, null, null, true);
        final Registration registration = new Registration();
        registration.setAvatar("");
        registration.setUsername(mobile);
        registration.setName(nickname);
        registration.setPassword(password);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        setResult(RESULT_OK);
                        AppManager.getAppManager().finishActivity(RegisterPass.this);
                        break;
                    case 0:
                        LogUtils.d((String)msg.obj);
                        showToast((String)msg.obj);
                        break;
                    case -1:
                        break;
                }
                dismissProgress(loadingPd);
            }
        };
        ExecutorService singleThreadExecutor = Executors.newSingleThreadExecutor();
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                final Message msg = new Message();
                try {
                    XmppConnectionManager.getInstance().register(registration, new XmppConnectionManager.XMPPCallback() {
                        @Override
                        public void onSuccess(Object object) {
                            baseApplication.setRegisterationInfo(registration);
                            msg.what = 1;
                        }

                        @Override
                        public void onFailure(Object message) {
                            msg.what = 0;
                            msg.obj = message;
                        }
                    });
                }
                catch (Exception e) {
                    msg.what = -1;
                }
                handler.sendMessage(msg);
            }
        });
    }
}
