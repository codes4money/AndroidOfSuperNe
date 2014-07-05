package com.donal.superne.app.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.Superne;
import com.donal.superne.app.client.Preferences;
import com.donal.superne.app.config.AppManager;
import com.donal.superne.app.service.MessageCenterService;
import com.donal.superne.app.ui.login.Login;
import com.donal.superne.app.ui.tabbar.Tabbar;

public class Welcome extends BaseActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        MessageCenterService.start(this);
        final View view = View.inflate(this, R.layout.activity_welcome, null);
        setContentView(view);
        AlphaAnimation aa = new AlphaAnimation(0.3f, 1.0f);
        aa.setDuration(2000);
        view.startAnimation(aa);

        aa.setAnimationListener(new Animation.AnimationListener()
        {
            public void onAnimationEnd(Animation arg0)
            {
                redirectTo();
            }

            public void onAnimationRepeat(Animation animation)
            {
            }

            public void onAnimationStart(Animation animation)
            {
            }

        });
    }


    private void redirectTo()
    {
//        MessageCenterService.start(Superne.getInstance());
        if (!Preferences.getPrefLoginFalg())
        {
            startActivity(new Intent(this, Login.class));
            AppManager.getAppManager().finishActivity(this);
        }
        else
        {
            Intent intent = new Intent(this, Tabbar.class);
            startActivity(intent);
            AppManager.getAppManager().finishActivity(this);
        }
    }

}
