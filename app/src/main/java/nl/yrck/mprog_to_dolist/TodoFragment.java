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
import android.widget.TextView;

import nl.yrck.mprog_to_dolist.adapters.TodoItemRecyclerAdapter;
import nl.yrck.mprog_to_dolist.storage.TodoItem;
import nl.yrck.mprog_to_dolist.storage.TodoList;
import nl.yrck.mprog_to_dolist.storage.TodoManager;
import nl.yrck.mprog_to_dolist.util.SimpleDividerItemDecoration;


public class TodoFragment extends Fragment {

    public static final String BUNDLE_LISTID = "LIST_ID";
    public static final String BUNDLE_LISTNAME = "LIST_NAME";
    public static String TAG = "TODO_FRAGMENT";
    Button buttonAdd;
    LinearLayoutManager linearLayoutManager;
    RecyclerView recyclerView;
    TodoItemRecyclerAdapter todoItemRecyclerAdapter;
    EditText editTextAdd;
    TodoList todoList;
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
            todoList = TodoManager.getInstance().getTodoList(listId);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_todo, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.reycler_view);
        buttonAdd = (Button) view.findViewById(R.id.add_todo_button);
        editTextAdd = (EditText) view.findViewById(R.id.add_todo_text);

        buttonAdd.setOnClickListener(v -> addTodo());

        linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        todoItemRecyclerAdapter = new TodoItemRecyclerAdapter(todoList.getTodoItems(), getActivity());
        todoItemRecyclerAdapter.setOnItemClickListener(new TodoItemRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View view) {
                toggleTodoStatus((Long) view.getTag());
            }

            @Override
            public void onItemLongClick(int position, View view) {
                TextView textView = (TextView) view.findViewById(R.id.todo_name);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Delete")
                        .setMessage("Delete item " + textView.getText().toString() + "?")
                        .setCancelable(true)
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .setPositiveButton("Delete", (dialog, which) -> deleteTodo((Long) view.getTag()));
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        recyclerView.setAdapter(todoItemRecyclerAdapter);

        return view;
    }

    private void addTodo() {
        TodoItem todoItem = new TodoItem(listId, editTextAdd.getText().toString());
        editTextAdd.setText("");
        TodoManager.getInstance().writeTodoItem(todoItem, todoList, getActivity());
        todoItemRecyclerAdapter.notifyDataSetChanged();
    }

    private void deleteTodo(long todoId) {
        TodoManager.getInstance().removeTodoItem(todoId, todoList, getActivity());
        todoItemRecyclerAdapter.notifyDataSetChanged();
    }

    private void toggleTodoStatus(long todoId) {
        TodoManager.getInstance().toggleTodoItem(todoId, todoList, getActivity());
        todoItemRecyclerAdapter.notifyDataSetChanged();
    }
}
