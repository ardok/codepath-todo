package com.codepath.simpletodo;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.codepath.simpletodo.adapter.TodoItemAdapter;
import com.codepath.simpletodo.constant.RequestCode;
import com.codepath.simpletodo.helper.FabToast;
import com.codepath.simpletodo.model.todoitem.TodoItem;
import com.codepath.simpletodo.model.todoitem.TodoItemDBHelper;

public class MainActivity extends AppCompatActivity {

    RecyclerView.LayoutManager mLayoutManager;
    RecyclerView mTodoRecyclerView;

    FloatingActionButton mFab;

    TodoItemAdapter mTodoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Make sure database is created
        TodoItemDBHelper.getInstance(this).getReadableDatabase();

        mTodoRecyclerView = (RecyclerView) findViewById(R.id.todoList);
        mFab = (FloatingActionButton) findViewById(R.id.fab);

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, TodoEditActivity.class);
                intent.putExtra("requestCode", RequestCode.TODO_NEW);
                MainActivity.this.startActivityForResult(intent, RequestCode.TODO_NEW);
            }
        });

        setUpTodoList();
    }

    private void setUpTodoList() {
        mTodoAdapter = new TodoItemAdapter(this, mTodoRecyclerView);
        mLayoutManager = new LinearLayoutManager(this);
        mTodoRecyclerView.setLayoutManager(mLayoutManager);
        mTodoRecyclerView.setAdapter(mTodoAdapter);

        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.LEFT || direction == ItemTouchHelper.RIGHT) {
                   int position = viewHolder.getAdapterPosition();
                   mTodoAdapter.removeItem(position);
                   FabToast.show(MainActivity.this, "Note deleted");
                }
            }
        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mTodoRecyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // REQUEST_CODE is defined above
        Bundle extras;
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case RequestCode.TODO_EDIT:
                    extras = data.getExtras();
                    if (extras != null) {
                        final TodoItem todoItem = (TodoItem) extras.getSerializable("todoItem");
                        final int listPosition = extras.getInt("listPosition", -1);
                        if (todoItem != null) {
                            FabToast.show(this, "Note edited");
                            if (listPosition > -1) {
                                mTodoAdapter.editItem(listPosition, todoItem);
                            }
                        }
                    }
                    break;

                case RequestCode.TODO_NEW:
                    extras = data.getExtras();
                    if (extras != null) {
                        final TodoItem todoItem = (TodoItem) extras.getSerializable("todoItem");
                        if (todoItem != null) {
                            FabToast.show(this, "Note created");
                            mTodoAdapter.addItem(todoItem);
                        }
                    }
                    break;
            }
        }
    }
}
