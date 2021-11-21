package com.example.noteapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class Database extends SQLiteOpenHelper implements Serializable {

    public static final String DATABASE_NAME = "NoteApplication.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "NoteApplication";
    public static final String COL_ID = "id";
    public static final String COL_TITLE = "Title";
    public static final String COL_DESCRIPTION = "Description";
    public static final String COL_DATE = "Date";
    public static final String IMAGE = "Image";
    byte[] image;


    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE "+TABLE_NAME+" ("+COL_ID+" INTEGER PRIMARY KEY AUTOINCREMENT," +
                " "+COL_TITLE+" TEXT,"+COL_DESCRIPTION+" TEXT, "+COL_DATE+" TEXT, "+IMAGE+" BLOB)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public Boolean insertData(ModelClass modelClass)
    {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(COL_TITLE,modelClass.getTitle());
        contentValues.put(COL_DESCRIPTION,modelClass.getDescription());
        contentValues.put(COL_DATE,modelClass.getDate());
        contentValues.put(IMAGE,modelClass.getImage());

        long inserted = sqLiteDatabase.insert(TABLE_NAME,null,contentValues);
        if (inserted>0)
        {
            return true;
        }else return false;
    }
    public ArrayList<ModelClass>getData()
    {
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        ArrayList<ModelClass>arrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.query(TABLE_NAME,null,null,null,
                null,null,null,null);
        if (cursor.moveToFirst())
        {
            do {
                int id = cursor.getInt(cursor.getColumnIndex(COL_ID));
                String title = cursor.getString(cursor.getColumnIndex(COL_TITLE));
                String description = cursor.getString(cursor.getColumnIndex(COL_DESCRIPTION));
                String date = cursor.getString(cursor.getColumnIndex(COL_DATE));

                byte[] bytes = cursor.getBlob(cursor.getColumnIndex(IMAGE));

                ModelClass modelClass = new ModelClass(id,title,description,date,bytes);
                arrayList.add(modelClass);
            }while (cursor.moveToNext());
            cursor.close();
        }
        return arrayList;

    }

    public Boolean deleteData(ModelClass modelClass)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        int delete = database.delete(TABLE_NAME,COL_ID+"=?",
                new String[]{String.valueOf(modelClass.getId())});
        if (delete>0)
        {
            return true;
        }else return false;
    }
    public Boolean updateData(ModelClass modelClass)
    {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_ID,modelClass.getId());
        contentValues.put(COL_TITLE,modelClass.getTitle());
        contentValues.put(COL_DESCRIPTION,modelClass.getDescription());
        int delete = database.update(TABLE_NAME,contentValues,COL_ID+"=?",
                new String[]{String.valueOf(modelClass.getId())});
        if (delete>0)
        {
            return true;
        }else return false;

    }
}
