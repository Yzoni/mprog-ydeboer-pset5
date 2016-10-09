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
        todoLists = dbHelper.getAllTodoListsPopulated();
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

    public TodoList getTodoList(long listId) {
        for (TodoList todoList : todoLists) {

            if (todoList.getId() == listId) {
                return todoList;
            }
        }
        return null;
    }

    public void writeTodoItem(TodoItem todo, TodoList todoList, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        long id = dbHelper.createTodo(todo);
        todoList.addTodoItem(dbHelper.getTodo(id));
    }

    public void writeTodoList(TodoList todoList, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        long id = dbHelper.createTodoList(todoList);
        this.todoLists.add(dbHelper.getTodoList(id));
    }

    public void removeTodoList(long listId, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        TodoList todoList = dbHelper.getTodoList(listId);
        for (TodoItem todo : todoList.getTodoItems()) {
            dbHelper.deleteTodoItem(todo.getId());
        }
        dbHelper.deleteTodoList(listId);
        this.todoLists.remove(todoList);
    }

    public void removeTodoItem(long id, TodoList todoList, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        todoList.getTodoItems().remove(dbHelper.getTodo(id));
        dbHelper.deleteTodoItem(id);
    }

    public void toggleTodoItem(long id, TodoList todoList, Context context) {
        DBHelper dbHelper = new DBHelper(context);
        TodoItem todoItem = dbHelper.getTodo(id);

        int indexOfTodo = todoList.getTodoItems().indexOf(todoItem);
        todoList.getTodoItems().get(indexOfTodo);
        if (todoItem.getStatus() == TodoItem.STATUS_CHECKED) {
            todoItem.setStatus(TodoItem.STATUS_UNCHECKED);
        } else {
            todoItem.setStatus(TodoItem.STATUS_CHECKED);
        }

        todoList.getTodoItems().set(indexOfTodo, todoItem);
        dbHelper.updateTodo(todoItem);
    }
}
