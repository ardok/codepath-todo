package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.simpletodo.constant.RequestCode;
import com.codepath.simpletodo.helper.Formatter;
import com.codepath.simpletodo.helper.Input;
import com.codepath.simpletodo.model.todoitem.TodoItem;

public class TodoEditActivity extends AppCompatActivity {
    TextView mTodoTitle;
    TextView mTodoDate;
    Button mTodoSaveBtn;
    EditText mTodoEditText;

    boolean isEditing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mTodoSaveBtn = (Button) findViewById(R.id.todoSaveBtn);
        mTodoEditText = (EditText) findViewById(R.id.todoEditText);
        mTodoTitle = (TextView) findViewById(R.id.todoEditTitle);
        mTodoDate = (TextView) findViewById(R.id.todoDate);

        Intent activityIntent = getIntent();
        final int requestCode = getIntent().getIntExtra("requestCode", -1);
        isEditing = requestCode == RequestCode.TODO_EDIT;

        if (isEditing) {
            setUpForEditing(activityIntent);
        } else {
            setUpForNew();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Input.closeSoftKeyboard(this);
    }

    private void setUpForEditing(Intent activityIntent) {
        final TodoItem todoItem = (TodoItem) activityIntent.getSerializableExtra("todoItem");
        if (todoItem == null) {
            Toast.makeText(this, "No todo item found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        final int listPosition = activityIntent.getIntExtra("listPosition", -1);

        mTodoTitle.setText(String.format("Editing note: %s", todoItem.getUUID()));
        mTodoEditText.setText(todoItem.getText());
        mTodoDate.setText(Formatter.formatCalendar(todoItem.getDate()));

        mTodoSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the modified data
                String text = mTodoEditText.getText().toString().trim();
                todoItem.setText(text);
                Intent intent = new Intent();
                intent.putExtra("todoItem", todoItem);
                intent.putExtra("listPosition", listPosition);
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setUpForNew() {
        mTodoTitle.setText("Creating new note");
        mTodoDate.setVisibility(View.GONE);

        mTodoSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mTodoEditText.getText().toString().trim();
                if (text.isEmpty()) {
                    Toast.makeText(TodoEditActivity.this, "Cannot save empty item", Toast.LENGTH_SHORT)
                         .show();
                    return;
                }
                Input.closeSoftKeyboard(TodoEditActivity.this);
                TodoItem newItem = new TodoItem(text);
                Intent intent = new Intent();
                intent.putExtra("todoItem", newItem);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        Input.openSoftKeyboard(mTodoEditText, TodoEditActivity.this);
    }
}
