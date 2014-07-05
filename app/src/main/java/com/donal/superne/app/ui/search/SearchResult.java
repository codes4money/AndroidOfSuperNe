package com.donal.superne.app.ui.search;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.donal.superne.app.BaseActivity;
import com.donal.superne.app.R;
import com.donal.superne.app.bean.UserListEntity;
import com.donal.superne.app.config.AppManager;
import com.donal.superne.app.config.Constant;
import com.donal.superne.app.ui.search.adapter.SearchResultAdapter;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class SearchResult extends BaseActivity implements View.OnClickListener, AdapterView.OnItemClickListener
{

    @ViewInject(R.id.lvResult)
    private ListView lvResult;

    private UserListEntity userListEntity;
    private SearchResultAdapter searchResultAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        userListEntity = (UserListEntity) getIntent().getSerializableExtra(Constant.USERS);
        ViewUtils.inject(this);
        initNavgation();
    }

    @Override
    protected void initNavgation()
    {
        super.initNavgation();
        setBtnLeft(this);
        setNavTitle(R.string.search_result);
        searchResultAdapter = new SearchResultAdapter(this, userListEntity.getUsers());
        lvResult.setAdapter(searchResultAdapter);
        lvResult.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.btnLeft:
                AppManager.getAppManager().finishActivity(this);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l)
    {

    }
}
