package net.justudio.acjunk.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import net.justudio.acjunk.model.AcItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/12/29 0029.
 */
public class DB extends SQLiteOpenHelper {

    private static final String TAG="AC DB";
    private static final String DATABASE_NAME="ac.db";
    private static final int DATABASE_VERSION =1;
    private static final String TABLES_NAME="acTable";

    public DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.e(TAG, "create database");
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e(TAG, "onCreate DB");
        db.execSQL("CREATE TABLE " + TABLES_NAME + " (" + "_ID"
                + " INTEGER PRIMARY KEY," + "title" + " TEXT," + "content"
                + " TEXT," + "date" + " TEXT," + "commentNum" + " TEXT," + "link"
                + " TEXT," + "acType" + " INTEGER" + ");");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.e(TAG,"delete DB");
        db.execSQL("DROP TABLE IF EXISTS acTable");
        onCreate(db);

    }

    public void insert(List<AcItem> list) {
        SQLiteDatabase db = this.getWritableDatabase();
        for(AcItem item : list){
            ContentValues values =new ContentValues();
            values.put("title",item.getTitle());
            values.put("content", item.getContent());
            values.put("date",item.getDate());
            values.put("commentNum", item.getCommentNum());
            values.put("acType", item.getType());

            db.insert("acTable", null,values);
        }
    }

    public void delete(int acType){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from acTable where acType='"+acType+"'");
    }

    //查询数据

    public List<AcItem> query(int acType) {
        List<AcItem> list = new ArrayList<AcItem>();
        SQLiteDatabase db = this.getReadableDatabase();
        String sql="select * from acTable where acType = '" + acType
                +"'";
        Cursor cursor = db.rawQuery(sql,null);
        cursor.moveToFirst();
        if(cursor.getCount()>0) {
            do {
                AcItem item = new AcItem();
                item.setTitle(cursor.getString(1));
                item.setContent(cursor.getString(2));
                item.setDate(cursor.getString(3));
                item.setCommentNum(cursor.getString(4));
                item.setLink(cursor.getString(5));
                item.setType(acType);

                list.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return list;
    }

}
