package com.stats.chinastats.database;

import com.stats.chinastats.util.Const;
import com.stats.chinastats.util.SDHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter
{
  private static final String DATABASE_CREATE = "create table collection (id integer primary key,title text not null,contentUrl text not null,imgSrc text,datetime text not null);";
  private static final String DATABASE_NAME =SDHelper.getSDPATH()+ Const.CACHE_PATH+"/collectionnews";
  private static final String DATABASE_TABLE = "collection";
  private static final int DATABASE_VERSION = 1;
  public static final String KEY_CONTENT_URL = "contentUrl";
  public static final String KEY_ID = "id";
  public static final String KEY_TITLE = "title";
  public static final String KEY_IMGSRC = "imgSrc";
private static final String TAG = "DBAdapter";
  private DatabaseHelper DBHelper;
  private final Context context;
  private SQLiteDatabase db;

  public DBAdapter(Context paramContext)
  {
    this.context = paramContext;
    DatabaseHelper localDatabaseHelper = new DatabaseHelper();
    this.DBHelper = localDatabaseHelper;
  }

  public void close()
  {
    this.DBHelper.close();
  }

  public boolean delete(String id)
  {
    String str = "id=" + id;
    int ret = this.db.delete(DATABASE_TABLE, str, null);
    if(ret>0 ) return true;
    else  return false;
  }

  public Cursor getAll()
  {
    SQLiteDatabase localSQLiteDatabase = this.db;
    String[] arrayOfString1 = new String[5];
    arrayOfString1[0] = "id";
    arrayOfString1[1] = "title";
    arrayOfString1[2] = "contentUrl";
    arrayOfString1[3] = "imgSrc";
    arrayOfString1[4] = "datetime";
      String[] arrayOfString2 = null;
    String str1 = null;
    String str2 = null;
    String str3 = "datetime desc";
    return localSQLiteDatabase.query(DATABASE_TABLE, arrayOfString1, null, arrayOfString2, str1, str2, str3);
  }

  public Cursor getTitle(String paramString)
    throws SQLException
  {
    SQLiteDatabase localSQLiteDatabase = this.db;
    String[] arrayOfString = new String[4];
    arrayOfString[0] = "id";
    arrayOfString[1] = "title";
    arrayOfString[2] = "contentUrl";
    arrayOfString[3] = "imgSrc";
    arrayOfString[3] = "datetime";
    String str1 = "id=" + paramString;
    String str2 = null;
    String str3 = null;
    String str4 = null;
    String str5 = null;
    Cursor localCursor = localSQLiteDatabase.query(true, DATABASE_TABLE, arrayOfString, str1, null, str2, str3, str4, str5);
    if (localCursor != null)
      localCursor.moveToFirst();
    return localCursor;
  }

  public long insert(String id, String title, String contentUrl,String imgSrc,String datetime)
  {
    ContentValues localContentValues = new ContentValues();
    localContentValues.put("id", id);
    localContentValues.put("title", title);
    localContentValues.put("contentUrl", contentUrl);
    localContentValues.put("imgSrc", imgSrc);
    localContentValues.put("datetime", datetime);
    return this.db.insert(DATABASE_TABLE, null, localContentValues);
    
  }

  public DBAdapter open()
    throws SQLException
  {
    SQLiteDatabase localSQLiteDatabase = this.DBHelper.getWritableDatabase();
    this.db = localSQLiteDatabase;
    return this;
  }

  class DatabaseHelper extends SQLiteOpenHelper
  {
    DatabaseHelper()
    {
      super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase)
    {
      paramSQLiteDatabase.execSQL(DATABASE_CREATE);
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2)
    {
      String str = "Upgrading database from version " + paramInt1 + " to " + paramInt2 + ", which will destroy all old data";
      Log.w("DBAdapter", str);
      paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS collection");
      onCreate(paramSQLiteDatabase);
    }
  }
}