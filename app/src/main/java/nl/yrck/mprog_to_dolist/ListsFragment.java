package nl.yrck.mprog_to_dolist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import nl.yrck.mprog_to_dolist.adapters.TodoListRecyclerAdapter;
import nl.yrck.mprog_to_dolist.dialogs.AddListDialog;
import nl.yrck.mprog_to_dolist.dialogs.RemoveListDialog;
import nl.yrck.mprog_to_dolist.storage.TodoList;
import nl.yrck.mprog_to_dolist.storage.TodoManager;
import nl.yrck.mprog_to_dolist.util.SimpleDividerItemDecoration;

public class ListsFragment extends Fragment {

    public static String TAG = "LIST_FRAGMENT";

    RecyclerView recyclerView;
    TodoListRecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    public ListsFragment() {
        // Required empty public constructor
    }

    public static ListsFragment newInstance() {
        return new ListsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TodoManager.getInstance().readTodos(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(v -> addListDialog());

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));

        recyclerAdapter = new TodoListRecyclerAdapter(
                TodoManager.getInstance().getTodoLists(),
                getActivity()
        );
        recyclerAdapter.setOnItemClickListener(new TodoListRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                startTodoActivity(v);
            }

            @Override
            public void onItemLongClick(int position, View view) {
                removeListDialog((Long) view.getTag());
            }
        });

        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }

    private void addListDialog() {
        AddListDialog addListDialog = new AddListDialog(this::onAddNewList);
        addListDialog.show(getActivity());
    }

    private void onAddNewList(String name) {
        TodoManager.getInstance().writeTodoList(new TodoList(name), getActivity());
        recyclerAdapter.notifyDataSetChanged();
    }

    private void removeListDialog(long listId) {
        RemoveListDialog removeListDialog = new RemoveListDialog(this::onRemoveList);
        removeListDialog.show(listId, getActivity());
    }

    private void onRemoveList(long listId) {
        TodoManager.getInstance().removeTodoList(listId, getActivity());
        recyclerAdapter.notifyDataSetChanged();
    }

    private void startTodoActivity(View v) {
        Intent intent = new Intent(getActivity(), TodoActivity.class);
        Bundle bundle = new Bundle();
        TextView textView = (TextView) v.findViewById(R.id.todo_name);
        bundle.putString(TodoFragment.BUNDLE_LISTNAME, textView.getText().toString());
        bundle.putLong(TodoFragment.BUNDLE_LISTID, (Long) v.getTag());
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
