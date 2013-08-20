package com.yulebaby.teacher.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBOpenHelper extends SQLiteOpenHelper {

	private final static String DB_NAME = "yulebaby.db";
	public final String TABLE_NAME = "template";
	public final String CONTENT = "content";

	/**
	 * @param context
	 *            上下文
	 * @param name
	 *            数据库的名字
	 * @param factory
	 *            游标工厂 处理结果集
	 * @param version
	 *            数据库的版本
	 */
	public DBOpenHelper(Context context) {
		super(context, DB_NAME, null, 1);
	}

	/**
	 * oncreate方法只有在第一次创建数据库的时候 才会被调用 作方便 做数据库的初始化操作
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table "
				+ TABLE_NAME
				+ " (id integer primary key autoincrement , content text)";
		db.execSQL(sql);
	}

	/**
	 * onupgrade这个方法 只有数据的版本发生改变的时候才会被调用
	 */
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// db.execSQL("alter table message add account varchar(20) null");
		if (newVersion > oldVersion) {
			db.execSQL("drop table if exists " + TABLE_NAME);
			onCreate(db);
		}

	}
}
