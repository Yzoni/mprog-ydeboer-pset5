package nl.yrck.mprog_to_dolist.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import nl.yrck.mprog_to_dolist.R;
import nl.yrck.mprog_to_dolist.storage.TodoItem;


public class TodoItemRecyclerAdapter extends RecyclerView.Adapter<TodoItemRecyclerAdapter.ViewHolder> {
    private static ClickListener clickListener;
    private List<TodoItem> todoItems;
    private Context context;

    public TodoItemRecyclerAdapter(List<TodoItem> myDataset, Context myContext) {
        todoItems = myDataset;
        context = myContext;
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        TodoItemRecyclerAdapter.clickListener = clickListener;
    }

    @Override
    public TodoItemRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                 int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item, parent, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        TodoItem todoItem = todoItems.get(position);

        holder.todoName.setText(todoItem.getName());

        if (todoItem.getStatus() == TodoItem.STATUS_CHECKED) {
            holder.todoName.setPaintFlags(holder.todoName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.todoName.setPaintFlags(holder.todoName.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
        }

        holder.itemView.setTag(todoItem.getId());
    }

    @Override
    public int getItemCount() {
        return todoItems.size();
    }

    public interface ClickListener {
        void onItemClick(int position, View v);

        void onItemLongClick(int position, View view);
    }

    static class ViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener,
            View.OnLongClickListener {
        TextView todoName;

        ViewHolder(View v) {
            super(v);
            todoName = (TextView) v.findViewById(R.id.todo_name);
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
