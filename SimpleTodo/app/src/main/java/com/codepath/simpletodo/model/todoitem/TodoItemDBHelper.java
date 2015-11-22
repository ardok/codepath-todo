package com.codepath.simpletodo.model.todoitem;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class TodoItemDBHelper extends SQLiteOpenHelper {
    private static TodoItemDBHelper instance = null;

    public static TodoItemDBHelper getInstance(Context context){
        if (instance == null ) {
            instance = new TodoItemDBHelper(context);
        }
        return instance;
    }

    private TodoItemDBHelper(Context context) {
        super(context, TodoItemDBContract.DATABASE_NAME, null, TodoItemDBContract.DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TodoItemDBContract.SQL_CREATE_TODO);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TodoItemDBContract.SQL_DELETE_TODO);
        onCreate(db);
    }
}
