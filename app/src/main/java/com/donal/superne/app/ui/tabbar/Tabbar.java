package com.donal.superne.app.ui.tabbar;

import android.app.TabActivity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TabHost;

import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.ui.contact.Contact;
import com.donal.superne.app.ui.conversation.Conversation;
import com.donal.superne.app.ui.discover.Discover;
import com.donal.superne.app.ui.profile.Profile;

public class Tabbar extends TabActivity
{
    private TabHost tabHost;
    private Intent intentConversation;
    private Intent intentContact;
    private Intent intentDiscover;
    private Intent intentProfile;
    private final static String TAB_TAG_CONVERSATION = "tab_tag_conversation";
    private final static String TAB_TAG_CONTACT = "tab_tag_contact";
    private final static String TAB_TAG_DISCOVER = "tab_tag_discover";
    private final static String TAB_TAG_PROFILE = "tab_tag_profile";

    private RelativeLayout layout1;
    private RelativeLayout layout2;
    private RelativeLayout layout3;
    private RelativeLayout layout4;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabbar);
        prepareIntent();
        setupIntent();
        initUI();
    }

    private void initUI()
    {
        layout1 = (RelativeLayout) findViewById(R.id.radio_button1);
        layout1.setSelected(true);
        layout2 = (RelativeLayout) findViewById(R.id.radio_button2);
        layout2.setSelected(false);
        layout3 = (RelativeLayout) findViewById(R.id.radio_button3);
        layout3.setSelected(false);
        layout4 = (RelativeLayout) findViewById(R.id.radio_button4);
        layout4.setSelected(false);
    }

    private void prepareIntent()
    {
        intentConversation = new Intent(this, Conversation.class);
        intentContact = new Intent(this, Contact.class);
        intentDiscover = new Intent(this, Discover.class);
        intentProfile = new Intent(this, Profile.class);
    }

    private void setupIntent()
    {
        tabHost = getTabHost();
        TabHost localTabHost = tabHost;
        localTabHost.addTab(buildTabSpec(TAB_TAG_CONVERSATION, R.string.conversation, R.drawable.tab_coversation, intentConversation));
        localTabHost.addTab(buildTabSpec(TAB_TAG_CONTACT, R.string.contact, R.drawable.tab_contact, intentContact));
        localTabHost.addTab(buildTabSpec(TAB_TAG_DISCOVER, R.string.discover, R.drawable.tab_discover, intentDiscover));
        localTabHost.addTab(buildTabSpec(TAB_TAG_PROFILE, R.string.me, R.drawable.tab_profile, intentProfile));
    }

    /**
     * 构建TabHost的Tab页
     *
     * @param tag      标记
     * @param resLabel 标签
     * @param resIcon  图标
     * @param content  该tab展示的内容
     * @return 一个tab
     */
    private TabHost.TabSpec buildTabSpec(String tag, int resLabel, int resIcon, final Intent content)
    {
        return this.tabHost.newTabSpec(tag).setIndicator(getString(resLabel),
                getResources().getDrawable(resIcon)).setContent(content);
    }

    public void ButtonClick(View v)
    {
        switch (v.getId())
        {
            case R.id.radio_button1:
                layout1.setSelected(true);
                layout2.setSelected(false);
                layout3.setSelected(false);
                layout4.setSelected(false);
                this.tabHost.setCurrentTabByTag(TAB_TAG_CONVERSATION);
                break;
            case R.id.radio_button2:
                layout1.setSelected(false);
                layout2.setSelected(true);
                layout3.setSelected(false);
                layout4.setSelected(false);
                this.tabHost.setCurrentTabByTag(TAB_TAG_CONTACT);
                break;
            case R.id.radio_button3:
                layout1.setSelected(false);
                layout2.setSelected(false);
                layout3.setSelected(true);
                layout4.setSelected(false);
                this.tabHost.setCurrentTabByTag(TAB_TAG_DISCOVER);
                break;
            case R.id.radio_button4:
                layout1.setSelected(false);
                layout2.setSelected(false);
                layout3.setSelected(false);
                layout4.setSelected(true);
                this.tabHost.setCurrentTabByTag(TAB_TAG_PROFILE);
                break;
        }
    }

}
