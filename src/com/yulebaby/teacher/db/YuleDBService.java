package com.yulebaby.teacher.db;

import java.util.LinkedList;

import com.yulebaby.teacher.model.AppriTemplate;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

public class YuleDBService {
	/** 数据库中保存的最大数据数 */
	public static final int MAXNUM = 200;
	private Context context;

	// public static final String TABLE_NAME = "message";
	private final String ID = "id";
	private final String CONTENT = "content";

	// 数据库 增删改查的操作
	public YuleDBService(Context context) {
		this.context = context;
	}

	/**
	 * 保存某个AppriTemplate的数据到数据库 param msg 对象 插入操作
	 */
	// public void save(AppriTemplate msg) {
	// DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
	// SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
	// // db.execSQL("insert into AppriTemplate (name, phone) values(?,?)", new
	// // Object[]{AppriTemplate.getName(),AppriTemplate.getPhone()});
	// ContentValues values = new ContentValues();
	// values.put(CONTENT, msg.content);
	// values.put(Title, msg.title);
	// values.put(TIME, msg.time);
	// values.put(ISREAD, msg.isread);
	// values.put(TRACKINFO, msg.trackInfo);
	// values.put(TYPE, msg.type);
	// db.insert(TABLE_NAME, null, values); // insert into AppriTemplate (name)
	// values NULL
	// db.close();
	// }

	/**
	 * 保存Message列表的数据到数据库 param List<AppriTemplate> 对象 插入操作
	 */
	public void insert(String content) {
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("content", content);
		db.insert(dbOpenHelper.TABLE_NAME, null, values);
		// SQLiteStatement stmt = db.compileStatement("INSERT INTO " +
		// TABLE_NAME
		// + "(" + CONTENT + "," + Title + "," + TIME + "," + TYPE + ","
		// + TRACKINFO + "," + ISREAD + "," + CONTENTID
		// + ") VALUES(?,?,?,?,?,?,?)");
		// int len = AppriTemplates.size() - 1;
		// for (; len >= 0; len--) {
		// stmt.clearBindings();
		// System.out.println("insert db:" + AppriTemplates.get(len).content
		// + AppriTemplates.get(len).title
		// + AppriTemplates.get(len).time
		// + AppriTemplates.get(len).isread);
		// stmt.bindString(1, AppriTemplates.get(len).content);
		// stmt.bindString(2, AppriTemplates.get(len).title);
		// stmt.bindString(3, AppriTemplates.get(len).time);
		// stmt.bindString(4, AppriTemplates.get(len).type);
		// stmt.bindString(5, AppriTemplates.get(len).trackInfo);
		// stmt.bindString(6, AppriTemplates.get(len).isread);
		// stmt.bindLong(7, AppriTemplates.get(len).contentId);
		// stmt.executeInsert();
		// }
		deleteAuto();
		db.close();
	}

	/**
	 * 删除操作
	 * 
	 * @param id
	 *            AppriTemplate所对应的id
	 */
	public void delete(int id) {
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		// db.execSQL("delete from AppriTemplate where id=?", new Object[]{id});
		db.delete(dbOpenHelper.TABLE_NAME, "id=?", new String[] { id + "" });
		db.close();
	}

	/**
	 * 
	 * @Title: deleteAuto
	 * @Description: 自动删除超过最大消息数目的消息
	 * @return void 返回类型
	 * @throws
	 */
	public void deleteAuto() {
		if (getCount() > MAXNUM) {
			DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
			SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
			int n = 0;

			n = (int) (getCount() - MAXNUM);
			int i = db.delete(dbOpenHelper.TABLE_NAME, "id <" + getTopId(n),
					null);
			System.out.println("自动删除" + i + "条记录");

			db.close();
		} else {
			return;
		}
	}

	private int getTopId(int n) {
		System.out.println(" much maxnum:" + n);
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		Cursor mCursor = null;
		int topId = 0;
		mCursor = db.query(dbOpenHelper.TABLE_NAME, new String[] { ID }, null,
				null, null, null, "id desc", 1 + "," + MAXNUM);
		// mCursor =
		// db.rawQuery("select id from AppriTemplate order by id  limit 1 offset 100",
		// null);
		if (null != mCursor && !mCursor.isFirst()) {
			mCursor.moveToLast();
		}
		int idIndex = mCursor.getColumnIndexOrThrow(ID);
		topId = mCursor.getInt(idIndex);

		mCursor.close();
		db.close();
		return topId;
	}

	/**
	 * 查询AppriTemplate的操作
	 * 
	 * @param id
	 *            AppriTemplate所对应的id
	 * @return
	 */
	public AppriTemplate find(int id) {
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		// Cursor cursor =
		// db.rawQuery("select * from AppriTemplate where id=? ",new
		// String[]{id+""});
		Cursor cursor = db.query(dbOpenHelper.TABLE_NAME, null, "id=?",
				new String[] { id + "" }, null, null, null);
		if (cursor.moveToFirst()) {
			// 获取name在结果集中的第几列
			int contentIndex = cursor.getColumnIndex(CONTENT);
			String content = cursor.getString(contentIndex);
			int idindex = cursor.getColumnIndex(ID);
			int AppriTemplateid = cursor.getInt(idindex);

			AppriTemplate p = new AppriTemplate(AppriTemplateid, content);
			cursor.close();
			db.close();
			return p;
		}
		cursor.close();
		db.close();
		return null;

	}

	/**
	 * 获取数据库条目的总数
	 */
	public long getCount() {
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query(dbOpenHelper.TABLE_NAME,
				new String[] { "count(*)" }, null, null, null, null, null);
		// Cursor cursor = db.rawQuery("select count(*) from AppriTemplate",
		// null);
		if (cursor.moveToFirst()) {
			long total = cursor.getLong(0);
			cursor.close();
			db.close();
			return total;
		}
		cursor.close();
		db.close();
		return 0;
	}

	/**
	 * 分页查询数据库的方法
	 * 
	 * @param offset
	 *            从那条数目开始检索
	 * @param maxnum2
	 *            每次从数据库中取得的个数
	 */
	public LinkedList<AppriTemplate> getScollDate(int offset, long maxnum2) {
		LinkedList<AppriTemplate> AppriTemplates = new LinkedList<AppriTemplate>();
		DBOpenHelper dbOpenHelper = new DBOpenHelper(context);
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		Cursor cursor = db.query(dbOpenHelper.TABLE_NAME, null, null, null,
				null, null, "id desc", offset + "," + maxnum2);
		// Cursor cursor =
		// db.rawQuery("select * from AppriTemplate order by id asc limit ?,?",
		// new
		// String[]{offset+"",max+""});
		while (cursor.moveToNext()) {
			int nameindex = cursor.getColumnIndex(CONTENT);
			String content = cursor.getString(nameindex);
			int idindex = cursor.getColumnIndex(ID);
			int AppriTemplateid = cursor.getInt(idindex);

			AppriTemplate p = new AppriTemplate(AppriTemplateid, content);

			// AppriTemplate p = new AppriTemplate(AppriTemplateid, name, title,
			// isread, time);
			AppriTemplates.add(p);
		}
		cursor.close();
		db.close();
		return AppriTemplates;
	}

}
