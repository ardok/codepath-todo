package com.codepath.simpletodo.model.todoitem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.UUID;

public class TodoItemDAO {
    private Context context;

    public TodoItemDAO(Context context) {
        this.context = context;
    }

    public long save(TodoItem todoItem) {
        TodoItemDBHelper helper = TodoItemDBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoItemDBContract.TodoItem.COLUMN_NAME_TEXT, todoItem.getText());
        values.put(TodoItemDBContract.TodoItem.COLUMN_NAME_DATE, todoItem.getDate().getTimeInMillis() / 1000);
        values.put(TodoItemDBContract.TodoItem.COLUMN_NAME_UUID, todoItem.getUUID().toString());

        return db.insert(TodoItemDBContract.TodoItem.TABLE_NAME, null, values);
    }

    public long update(TodoItem todoItem) {
        TodoItemDBHelper helper = TodoItemDBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(TodoItemDBContract.TodoItem.COLUMN_NAME_TEXT, todoItem.getText());
        values.put(TodoItemDBContract.TodoItem.COLUMN_NAME_DATE, todoItem.getDate().getTimeInMillis() / 1000);
        values.put(TodoItemDBContract.TodoItem.COLUMN_NAME_UUID, todoItem.getUUID().toString());

        String[] whereArgs = {todoItem.getUUID().toString()};
        return db.update(TodoItemDBContract.TodoItem.TABLE_NAME,
                values,
                "uuid=?",
                whereArgs);
    }

    public int delete(TodoItem todoItem) {
        TodoItemDBHelper helper = TodoItemDBHelper.getInstance(context);
        SQLiteDatabase db = helper.getWritableDatabase();

        String selection = TodoItemDBContract.TodoItem.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = { String.valueOf(todoItem.getId()) };

        return db.delete(TodoItemDBContract.TodoItem.TABLE_NAME, selection, selectionArgs);
    }

    public List<TodoItem> getAll() {
        TodoItemDBHelper helper = TodoItemDBHelper.getInstance(context);
        SQLiteDatabase db = helper.getReadableDatabase();

        String[] projection = {
            TodoItemDBContract.TodoItem.COLUMN_NAME_ID,
            TodoItemDBContract.TodoItem.COLUMN_NAME_TEXT,
            TodoItemDBContract.TodoItem.COLUMN_NAME_DATE,
            TodoItemDBContract.TodoItem.COLUMN_NAME_UUID
        };

        String sortOrder = TodoItemDBContract.TodoItem.COLUMN_NAME_DATE + " DESC";
        Cursor c = db.query(
                TodoItemDBContract.TodoItem.TABLE_NAME,        // The table to query
                projection,                             // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                sortOrder                               // The sort order
        );

        List<TodoItem> todoItems = new ArrayList<>();

        while (c.moveToNext()) {
            long id = c.getLong(c.getColumnIndex(TodoItemDBContract.TodoItem.COLUMN_NAME_ID));
            String uuid = c.getString(c.getColumnIndex(TodoItemDBContract.TodoItem.COLUMN_NAME_UUID));
            String text = c.getString(
                    c.getColumnIndex(TodoItemDBContract.TodoItem.COLUMN_NAME_TEXT));
            Calendar date = new GregorianCalendar();
            date.setTimeInMillis(c.getLong(c.getColumnIndex(
                    TodoItemDBContract.TodoItem.COLUMN_NAME_DATE)) * 1000);
            todoItems.add(new TodoItem(id, text, date, UUID.fromString(uuid)));
        }

        c.close();

        return todoItems;
    }
}
