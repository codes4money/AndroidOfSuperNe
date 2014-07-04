package com.donal.superne.app.manager;

import android.database.Cursor;
import com.donal.superne.app.BaseApplication;
import com.donal.superne.app.ui.contact.adapter.ContactAdapter;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import org.jivesoftware.smackx.superne.User;

/**
 * Created by donal on 14-7-4.
 */
public class DatabaseManager {

    private static DatabaseManager databaseManager;
    private static DbUtils dbUtils;

    public static DatabaseManager getInstance() {
        if (databaseManager == null) {
            databaseManager = new DatabaseManager();
        }
        return databaseManager;
    }

    public DatabaseManager() {
        try {
            dbUtils = DbUtils.create(BaseApplication.getInstance(), BaseApplication.getInstance().getRegisterationInfo().getUsername());
            dbUtils.configAllowTransaction(true);
            dbUtils.configDebug(true);
            dbUtils.createTableIfNotExist(User.class);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DbUtils getDB() {
        return dbUtils;
    }

}
