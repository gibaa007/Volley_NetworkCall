package com.nagainfo.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.volley.Response;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nagainfo on 30/3/16.
 */
public class InsertData extends SQLiteOpenHelper {
    private static int DB_VERSION =1;
    private static  String DB_NAME ="web";
    private static  String TABLE_NAME ="film";
    private  static String ID ="_id";
    private  static String TITLE ="title";
    private static  String IMG ="image";



    public InsertData(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DB_NAME,null,DB_VERSION);
    }
    public InsertData(Context con){
        super(con,DB_NAME,null,DB_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_qry   = "CREATE TABLE TEST (id INTEGER PRIMARY KEY)";
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + ID + " INTEGER PRIMARY KEY," + TITLE + " TEXT,"
                + IMG + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    }
    public void addData(Data data){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues =new ContentValues();
        contentValues.put(TITLE,data.getTitle());
        contentValues.put(IMG,data.getImg());
        long id=db.insert(TABLE_NAME, null, contentValues);
        db.close();

    }
    public List<Data> getSavedData(String search_val){
        List<Data> savedData =new ArrayList<Data>();
        String select_qry   ="SELECT * FROM "+TABLE_NAME + " WHERE "+ TITLE +"= "+search_val;
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor   =db.rawQuery(select_qry,null);
        if(cursor.moveToFirst()){
            do {
                Data data=new Data();
                data.setTitle(cursor.getString(1));
                data.setTitle(cursor.getString(2));
                savedData.add(data);

            }while (cursor.moveToNext());
        }
        return savedData;
    }

}
