package com.donal.superne.app.manager;

import com.lidroid.xutils.DbUtils;

/**
 * Created by donal on 14-7-4.
 */
public class DBManager
{
    private static DbUtils db;

    public static DbUtils getDb()
    {
        return db;
    }

    public static void setDb(DbUtils db)
    {
        DBManager.db = db;
    }
}
