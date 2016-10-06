package nl.yrck.mprog_to_dolist.adapters;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.yrck.mprog_to_dolist.R;
import nl.yrck.mprog_to_dolist.storage.TodoItem;
import nl.yrck.mprog_to_dolist.storage.TodoList;


public class TodoListRecyclerAdapter extends RecyclerView.Adapter<TodoListRecyclerAdapter.ViewHolder> {
    private static ClickListener clickListener;
    private List<TodoList> todoLists;
    private Context context;

    public TodoListRecyclerAdapter(List<TodoList> myDataset, Context myContext) {
        todoLists = myDataset;
        context = myContext;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TodoListRecyclerAdapter.clickListener = clickListener;
    }

    @Override
    public TodoListRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TodoList todoList = todoLists.get(position);
        holder.todoListName.setText(todoList.getName());

        holder.itemView.setTag(todoList.getId());
    }

    @Override
    public int getItemCount() {
        return todoLists.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener {

        TextView todoListName;

        ViewHolder(View v) {
            super(v);
            todoListName = (TextView) v.findViewById(R.id.todo_name);
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(getAdapterPosition(), view);
            return false;
        }
    }


}
