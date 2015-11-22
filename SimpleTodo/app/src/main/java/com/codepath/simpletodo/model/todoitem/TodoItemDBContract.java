package com.codepath.simpletodo.model.todoitem;

import android.provider.BaseColumns;

public class TodoItemDBContract {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "simpletodo";

    private TodoItemDBContract() {}

    /* CREATE TABLE Statements */
    public static final String SQL_CREATE_TODO = String.format(
            "CREATE TABLE %s (%s TEXT, %s TEXT, %s DATETIME)",
            TodoItem.TABLE_NAME,
            TodoItem.COLUMN_NAME_UUID,
            TodoItem.COLUMN_NAME_TEXT, 
            TodoItem.COLUMN_NAME_DATE);

    /* DROP TABLE Statements */
    public static final String SQL_DELETE_TODO =
            String.format("DROP TABLE IF EXISTS %s", TodoItem.TABLE_NAME);

    /* Inner class that defines the note table */
    public static abstract class TodoItem implements BaseColumns {
        public static final String TABLE_NAME = "todoitem";
        public static final String COLUMN_NAME_ID = "rowid";
        public static final String COLUMN_NAME_UUID = "uuid";
        public static final String COLUMN_NAME_TEXT = "text";
        public static final String COLUMN_NAME_DATE = "date";
    }
}
