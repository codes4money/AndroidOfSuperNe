package com.donal.superne.app.ui.search.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.donal.superne.app.R;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import org.jivesoftware.smackx.superne.User;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by donal on 14-7-4.
 */
public class SearchResultAdapter extends BaseAdapter {

    private Context context;
    private List<User> users;
    private LayoutInflater layoutInflater;
    private DisplayImageOptions displayImageOptions;

    static class CellHolder {
        ImageView imgAvatar;
        TextView tvName;
        TextView tvNickname;
    }

    public SearchResultAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
        this.layoutInflater = LayoutInflater.from(context);
        displayImageOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
//                .showImageOnLoading(R.drawable.icon_topic)
//                .showImageForEmptyUri(R.drawable.icon_topic)
//                .showImageOnFail(R.drawable.icon_topic)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)
                .displayer(new RoundedBitmapDisplayer(10))
                .build();
    }

    @Override
    public int getCount() {
        return users.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CellHolder cellHolder;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.cell_search_result, null);
            cellHolder = new CellHolder();
            cellHolder.imgAvatar = (ImageView) view.findViewById(R.id.imgAvatar);
            cellHolder.tvName = (TextView) view.findViewById(R.id.tvName);
            cellHolder.tvNickname = (TextView) view.findViewById(R.id.tvNicknname);
        }
        else {
            cellHolder = (CellHolder) view.getTag();
        }
        view.setTag(cellHolder);
        User user = users.get(i);
        ImageLoader.getInstance().displayImage(user.getAvatar(), cellHolder.imgAvatar, displayImageOptions);
        cellHolder.tvName.setText(user.getUsername());
        cellHolder.tvNickname.setText(user.getName());
        return view;
    }
}
