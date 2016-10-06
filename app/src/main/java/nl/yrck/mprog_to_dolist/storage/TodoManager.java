package nl.yrck.mprog_to_dolist.storage;


import android.content.Context;

import java.util.ArrayList;
import java.util.List;

public class TodoManager {

    private static TodoManager todoManager;

    private List<TodoList> todoLists;

    private TodoManager() {
        todoLists = new ArrayList<>();
    }

    public static TodoManager getInstance() {
        if (todoManager == null) {
            todoManager = new TodoManager();
        }
        return todoManager;
    }

    public void readTodos(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        todoLists = dbHelper.getAllTodoLists();
    }

    public void writeTodos(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        dbHelper.clear();
        for (TodoList todoList : todoLists) {
            dbHelper.createTodoList(todoList);
        }
    }

    public List<TodoList> getTodoLists() {
        return todoLists;
    }

    public TodoList getTodoList(long list_id) {
        for (TodoList todoList : todoLists) {
            if (todoList.getId() == list_id) {
                return todoList;
            }
        }
        return null;
    }
}
