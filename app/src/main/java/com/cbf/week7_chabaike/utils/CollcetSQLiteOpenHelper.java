package com.cbf.week7_chabaike.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class CollcetSQLiteOpenHelper extends SQLiteOpenHelper {

	private Context context;
	private static String dbName = "MyCollect";
	private static int dbVersion = 1;
	private final String tableName = "mycollect";

	public String getTableName() {
		return tableName;
	}

	/**
	 *
	 * @param context ??????
	 * @param name ?????????
	 * @param factory ????Cursor?????????????????????????????????cursor????????null?????????????????
	 * @param version ????????
	 */
	public CollcetSQLiteOpenHelper(Context context, String name,
								   CursorFactory factory, int version) {
		super(context, name, factory, version);
	}


	public CollcetSQLiteOpenHelper(Context context) {
		super(context, dbName, null,dbVersion);
		this.context = context;
	}


	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table mycollect (_id integer primary key autoincrement,title varchar unique,id varchar,"+
				"create_time varchar,source varchar,author varchar,weiboUrl varchar)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("create table mysql_tow (_id integer primary key autoincrement,title varchar unique,content varchar,cover varchar)");

		db.execSQL("alter table mysql_one add salary integer");

		//		db.execSQL("drop table mysql_one if exists");



	}

}
