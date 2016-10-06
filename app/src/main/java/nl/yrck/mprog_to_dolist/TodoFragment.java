package nl.yrck.mprog_to_dolist;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.List;

import nl.yrck.mprog_to_dolist.adapters.TodoItemRecyclerAdapter;
import nl.yrck.mprog_to_dolist.storage.TodoItem;
import nl.yrck.mprog_to_dolist.storage.TodoManager;
import nl.yrck.mprog_to_dolist.util.SimpleDividerItemDecoration;


public class TodoFragment extends Fragment {

    public static final String BUNDLE_LISTID = "LIST_ID";
    public static String TAG = "TODO_FRAGMENT";
    Button buttonAdd;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    TodoItemRecyclerAdapter todoItemRecyclerAdapter;
    EditText editTextAdd;
    List<TodoItem> todoItems;
    private long listId;

    public TodoFragment() {
        // Required empty public constructor
    }

    public static TodoFragment newInstance(long listId) {
        TodoFragment fragment = new TodoFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(BUNDLE_LISTID, listId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            listId = getArguments().getLong(BUNDLE_LISTID);

            todoItems = TodoManager.getInstance().getTodoList(listId).getTodoItems();
        }
    }

    private void updateRecycler() {
        todoItems.clear();
        todoItems.addAll(TodoManager.getInstance().getTodoList(listId).getTodoItems());
        todoItemRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.reycler_view);
        buttonAdd = (Button) view.findViewById(R.id.add_todo_button);
        editTextAdd = (EditText) view.findViewById(R.id.add_todo_text);

        buttonAdd.setOnClickListener(v -> addTodo());

        todoItems = TodoManager.getInstance().getTodoList(listId).getTodoItems();

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        todoItemRecyclerAdapter = new TodoItemRecyclerAdapter(todoItems, getActivity());
        todoItemRecyclerAdapter.setOnItemClickListener(new TodoItemRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                toggleTodoStatus(position);
                updateRecycler();
            }

            @Override
            public void onItemLongClick(int position, View view) {
                TodoItem todoItem = todoItems.get(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete")
                        .setMessage("Delete todoItem " + todoItem.getName() + "?")
                        .setCancelable(true)
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Delete", (dialog, which) -> deleteTodo(position));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        recyclerView.setAdapter(todoItemRecyclerAdapter);

        return view;
    }

    private void addTodo() {
        todoItems.add(new TodoItem(listId, editTextAdd.getText().toString()));
    }

    private void deleteTodo(int position) {
        todoItems.remove(position);
    }

    private void toggleTodoStatus(int position) {
        TodoItem todo = todoItems.get(position);
        if (todo.getStatus() == TodoItem.STATUS_CHECKED) {
            todo.setStatus(TodoItem.STATUS_UNCHECKED);
        } else {
            todo.setStatus(TodoItem.STATUS_CHECKED);
        }
    }
}
