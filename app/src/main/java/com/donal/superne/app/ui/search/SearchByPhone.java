package com.donal.superne.app.ui.search;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.bean.UserListEntity;
import com.donal.superne.app.config.AppManager;
import com.donal.superne.app.config.Constant;
import com.donal.superne.app.manager.XmppConnectionManager;
import com.donal.superne.app.utils.StringUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.util.LogUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import org.jivesoftware.smackx.superne.User;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SearchByPhone extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.edtPhone)
    private EditText edtPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_by_phone);
        ViewUtils.inject(this);
        initNavgation();
    }

    @Override
    protected void initNavgation() {
        super.initNavgation();
        setNavTitle(R.string.search_by_phone);
        setBtnLeft(this);
        setBtnRight(this);
        getBtnRight().setText(R.string.search);
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
                search(0);
                break;
        }
    }

    private void search(final int page) {
        final String phone = edtPhone.getEditableText().toString();
        if (StringUtils.empty(StringUtils.doEmpty(phone))) {
            return;
        }
        loadingPd = showProgress(this, null, null, true);
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case 1:
                        List<User> users = (List<User>)msg.obj;
                        UserListEntity userListEntity = new UserListEntity();
                        userListEntity.setUsers(users);
                        startActivity(new Intent(SearchByPhone.this, SearchResult.class).putExtra(Constant.USERS, userListEntity));
                        break;
                    case 0:
                        showDialog((String) msg.obj);
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
                    XmppConnectionManager.getInstance().searchByUsername(phone, page, new XmppConnectionManager.XMPPCallback(){
                        @Override
                        public void onSuccess(Object object) {
                            msg.what = 1;
                            msg.obj = object;
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
