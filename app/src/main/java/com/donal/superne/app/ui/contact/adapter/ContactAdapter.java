package com.donal.superne.app.ui.contact.adapter;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;

import com.donal.superne.app.manager.DBManager;
import com.donal.superne.app.ui.contact.ContactCell;
import com.lidroid.xutils.db.sqlite.CursorUtils;

import org.jivesoftware.smackx.superne.User;

/**
 * Created by donal on 14-7-4.
 */
public class ContactAdapter extends CursorAdapter
{

    public ContactAdapter(Context context, Cursor c, boolean autoRequery)
    {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup)
    {
        return new ContactCell(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {
        User user = CursorUtils.getEntity(DBManager.getDb(),
                cursor,
                User.class,
                0);
        ((ContactCell) view).setUser(user);
    }
}
