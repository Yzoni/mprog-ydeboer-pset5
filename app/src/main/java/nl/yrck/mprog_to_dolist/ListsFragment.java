package nl.yrck.mprog_to_dolist;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import nl.yrck.mprog_to_dolist.adapters.TodoListRecyclerAdapter;
import nl.yrck.mprog_to_dolist.storage.TodoList;
import nl.yrck.mprog_to_dolist.storage.TodoManager;

public class ListsFragment extends Fragment {

    RecyclerView recyclerView;
    TodoListRecyclerAdapter recyclerAdapter;
    RecyclerView.LayoutManager layoutManager;

    List<TodoList> todoLists;

    public ListsFragment() {
        // Required empty public constructor
    }

    public static ListsFragment newInstance(long listId) {
        ListsFragment fragment = new ListsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todoLists = Util.convertLongParseArrayToList(TodoManager.getInstance().getTodoLists());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lists, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new TodoListRecyclerAdapter(todoLists, getActivity());
        recyclerAdapter.setOnItemClickListener(new TodoListRecyclerAdapter.ClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                startTodoActivity(v);
            }

            @Override
            public void onItemLongClick(int position, View view) {

            }
        });

        recyclerView.setAdapter(recyclerAdapter);
        return view;
    }

    private void startTodoActivity(View v) {
        Intent intent = new Intent(getActivity(), TodoActivity.class);
        startActivity(intent);
    }
}
