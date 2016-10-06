package nl.yrck.mprog_to_dolist.storage;


import android.content.Context;
import android.support.v4.util.LongSparseArray;

import java.util.List;

public class TodoManager {

    private static TodoManager todoManager;

    private LongSparseArray<TodoList> todoLists;

    private TodoManager() {
        todoLists = new LongSparseArray<>();
    }

    public static TodoManager getInstance() {
        if (todoManager == null) {
            todoManager = new TodoManager();
        }
        return todoManager;
    }

    public void readTodos(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        for (TodoList todoList : dbHelper.getAllTodoLists()) {
            todoLists.put(todoList.getId(), todoList);
        }
    }

    public void writeTodos(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        for (TodoList todoList : dbHelper.getAllTodoLists()) {
            dbHelper.createTodoList(todoList);
        }
    }

    public LongSparseArray<TodoList> getTodoLists() {
        return todoLists;
    }

    public List<TodoItem> getTodoItems(long listId) {
        return todoLists.get(listId).getTodoItems();
    }

}
