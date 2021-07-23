package com.example.sampleprice;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class SqliteDatabase extends SQLiteOpenHelper {

    private	static final int DATABASE_VERSION =	2;
    private	static final String	DATABASE_NAME = "KuberBazar";
    private	static final String TABLE_CONTACTS = "KuberBazarTable";

    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_NAME = "contactname";
    private static final String COLUMN_NO = "phno";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";



    public SqliteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String	CREATE_CONTACTS_TABLE = "CREATE	TABLE " + TABLE_CONTACTS + "(" + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT," + COLUMN_NO + " INTEGER," + COLUMN_DATE + " TEXT,"+ COLUMN_TIME + " TEXT"+")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        onCreate(db);
    }

    public ArrayList<Items> listContacts(){
        String sql = "select * from " + TABLE_CONTACTS + " ORDER BY  contactname COLLATE NOCASE "+"ASC "+"  ";
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<Items> storeContacts = new ArrayList<>();
        Cursor cursor = db.rawQuery(sql, null);
//        cursor = db.query(TABLE_CONTACTS,null,null,null,null,null,COLUMN_NAME+ " DESC ",null);
        if(cursor.moveToFirst()){
            do{
                int id = Integer.parseInt(cursor.getString(0));
                String name = cursor.getString(1);
                String phno = cursor.getString(2);
                String date = cursor.getString(3);
                String time = cursor.getString(4);
                storeContacts.add(new Items(id, name, phno,date,time));
            }while (cursor.moveToNext());
        }
        cursor.close();
        return storeContacts;
    }

    public void addContacts(Items items){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, items.getName());
        values.put(COLUMN_NO, items.getprice());
        values.put(COLUMN_DATE, items.getDate());
        values.put(COLUMN_TIME, items.getTime());
        SQLiteDatabase db = this.getWritableDatabase();
        db.insert(TABLE_CONTACTS, null, values);
    }

    public void updateContacts(Items items){
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, items.getName());
        values.put(COLUMN_NO, items.getprice());
        values.put(COLUMN_DATE, items.getDate());
        values.put(COLUMN_TIME, items.getTime());
        SQLiteDatabase db = this.getWritableDatabase();
        db.update(TABLE_CONTACTS, values, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(items.getId())});
    }

    public Items findContacts(String name){
        String query = "Select * FROM "	+ TABLE_CONTACTS + " WHERE " + COLUMN_NAME + " = " + name;
        SQLiteDatabase db = this.getWritableDatabase();
        Items items = null;
        Cursor cursor = db.rawQuery(query,	null);
        if	(cursor.moveToFirst()){
            int id = Integer.parseInt(cursor.getString(0));
            String contactsName = cursor.getString(1);
            String contactsNo = cursor.getString(2);
            String date = cursor.getString(3);
            String time = cursor.getString(4);
            items = new Items(id, contactsName, contactsNo,date,time);
        }
        cursor.close();
        return items;
    }

    public void deleteContact(int id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, COLUMN_ID	+ "	= ?", new String[] { String.valueOf(id)});
    }
}
