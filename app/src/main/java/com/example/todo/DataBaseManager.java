package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DataBaseManager extends SQLiteOpenHelper {
    public static final String tableName = "TASK_TABLE";
    public static final String column_title = "TITLE";
    public static final String column_desc = "DESCRIPTION";
    public static final String column_complete = "COMPLETE";
    public static final String column_id = "ID";

    private SQLiteDatabase sql;
    public DataBaseManager(@Nullable Context context) {
        super(context,"task.db" , null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + tableName + " (" + column_id + " INTEGER PRIMARY KEY AUTOINCREMENT , " + column_title + " TEXT  ," + column_desc + " TEXT , " + column_complete + " BOOL)";

        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    //add a new record
    public boolean addOne(Tasks tasks) {
        sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_title,tasks.getTitle());
        cv.put(column_desc,tasks.getDescription());
        cv.put(column_complete,tasks.isCompleteness());

        long insert = sql.insert(tableName,null,cv);
        return insert != -1;
    }

    //delete a record
    public boolean deleteOne(Tasks tasks) {
        SQLiteDatabase sql = this.getWritableDatabase();
        String queryString = "DELETE FROM " +tableName+ " WHERE " + column_id + " = "+tasks.getId();
        Cursor cursor = sql.rawQuery(queryString, null);
        return cursor.moveToFirst();
    }

    //get all rows from database
    public ArrayList<Tasks> getAll() {
        ArrayList<Tasks> returnList = new ArrayList<>();
        String queryString = "SELECT * FROM " +tableName;
        sql = this.getReadableDatabase();

        Cursor cursor = sql.rawQuery(queryString,null);

        if (cursor.moveToFirst()) {
            do {
                int id =cursor.getInt(0);
                String title = cursor.getString(1);
                String desc = cursor.getString(2);
                boolean isComplete = (cursor.getInt(3) == 1);

                Tasks task = new Tasks(id,title,desc,isComplete);
                returnList.add(task);
            }while (cursor.moveToNext());
        } else {
            returnList = null;
        }
        cursor.close();
        sql.close();
        return returnList;
    }

    //update completeness
    public boolean update(Tasks tasks) {
        sql = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(column_complete,tasks.isCompleteness());
        long update = sql.update(tableName,cv,column_id+" = ?",new String[]{String.valueOf(tasks.getId())});
        return update != -1;

    }
}
