package com.cbf.week7_chabaike.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2016/11/15.
 */

public class HistorySQLiteHelper extends SQLiteOpenHelper {

    private Context context;
    private static String dbName = "MyLisi";
    private static int dbVersion = 1;
    private final String tableName = "history";

    public String getTableName() {
        return tableName;
    }

    public HistorySQLiteHelper(Context context) {
        super(context, dbName, null, dbVersion);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table history (_id integer primary key autoincrement,title varcher unique,id varcher," +
                "create_time varcher,source varcher,author varcher,weiboUrl varcher)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
