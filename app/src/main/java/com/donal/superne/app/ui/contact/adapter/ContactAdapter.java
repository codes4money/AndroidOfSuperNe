package com.donal.superne.app.ui.contact.adapter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.donal.superne.app.R;
import com.donal.superne.app.manager.DatabaseManager;
import com.donal.superne.app.ui.contact.ContactCell;
import com.lidroid.xutils.db.sqlite.CursorUtils;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.BitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import org.jivesoftware.smackx.superne.User;

import java.util.List;

/**
 * Created by donal on 14-7-4.
 */
public class ContactAdapter extends CursorAdapter {

    public ContactAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return new ContactCell(context);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        User user = CursorUtils.getEntity(DatabaseManager.getInstance().getDB(),
                cursor,
                User.class,
                0);
        ((ContactCell) view).setUser(user);
    }
}
