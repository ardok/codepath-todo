package com.codepath.simpletodo.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.simpletodo.MainActivity;
import com.codepath.simpletodo.R;
import com.codepath.simpletodo.TodoEditActivity;
import com.codepath.simpletodo.constant.RequestCode;
import com.codepath.simpletodo.helper.FabToast;
import com.codepath.simpletodo.model.todoitem.TodoItem;
import com.codepath.simpletodo.model.todoitem.TodoItemDAO;

import java.util.ArrayList;

public class TodoItemAdapter extends RecyclerView.Adapter<TodoItemAdapter.ViewHolder> {

    private TodoItemDAO mTodoItemDAO;

    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private ArrayList<TodoItem> mTodoItems = new ArrayList<>();

    public TodoItemAdapter(Activity activity, RecyclerView recyclerView) {
        mActivity = activity;
        mRecyclerView = recyclerView;

        mTodoItemDAO = new TodoItemDAO(activity);
        mTodoItems = (ArrayList<TodoItem>) mTodoItemDAO.getAll();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.todo_item_adapter, viewGroup, false);
        final ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.setOnItemLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                // Get the position of v
                // Call the removeItem method with the position
                removeItem(mRecyclerView.getChildAdapterPosition(v));
                FabToast.show(mActivity, "Note deleted");
                return true;
            }
        });
        viewHolder.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int adapterPosition = mRecyclerView.getChildAdapterPosition(v);
                final TodoItem todoItem = mTodoItems.get(adapterPosition);
                Intent intent = new Intent(mActivity, TodoEditActivity.class);
                intent.putExtra("todoItem", todoItem);
                intent.putExtra("listPosition", adapterPosition);
                intent.putExtra("requestCode", RequestCode.TODO_EDIT);
                mActivity.startActivityForResult(intent, RequestCode.TODO_EDIT);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        final TodoItem todoItem = mTodoItems.get(viewHolder.getLayoutPosition());
        viewHolder.setText(todoItem.getText());
    }

    @Override
    public int getItemCount() {
        return mTodoItems.size();
    }

    public void addItem(TodoItem item) {
        // Add item
        mTodoItems.add(0, item);
        // Force scroll to top to show new item
        mRecyclerView.scrollToPosition(0);
        notifyItemInserted(0);
        mTodoItemDAO.save(item);
    }

    public void removeItem(int position) {
        TodoItem removedItem = mTodoItems.remove(position);
        notifyItemRemoved(position);
        mTodoItemDAO.delete(removedItem);
    }

    public void editItem(int position, TodoItem updatedItem) {
        mTodoItems.add(position, updatedItem);
        mTodoItems.remove(position + 1);
        notifyItemChanged(position);
        mTodoItemDAO.update(updatedItem);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView text;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            text = (TextView) itemView.findViewById(R.id.text);
        }

        public void setText(String text) {
            this.text.setText(text);
        }

        public void setOnItemLongClickListener(View.OnLongClickListener onLongClickListener) {
            cardView.setOnLongClickListener(onLongClickListener);
        }

        public void setOnItemClickListener(View.OnClickListener onClickListener) {
            cardView.setOnClickListener(onClickListener);
        }
    }
}
