package com.donal.superne.app.ui.contact;

import android.content.Context;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.donal.superne.app.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.jivesoftware.smackx.superne.User;

/**
 * Created by donal on 14-7-5.
 */
public class ContactCell extends RelativeLayout {

    @ViewInject(R.id.imgAvatar)
    private ImageView imgAvatar;

    @ViewInject(R.id.tvName)
    private TextView tvName;

    private User user;

    public ContactCell(Context context) {
        super(context);
        inflate(context, R.layout.cell_contact, null);
        ViewUtils.inject(this);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        tvName.setText(user.getName());
        ImageLoader.getInstance().displayImage(user.getAvatar(), imgAvatar);
    }
}
